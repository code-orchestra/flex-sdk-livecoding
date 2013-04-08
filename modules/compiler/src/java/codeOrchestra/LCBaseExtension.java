package codeOrchestra;

import codeOrchestra.tree.*;
import flex2.compiler.CompilationUnit;
import flex2.tools.Fcsh;
import macromedia.asc.parser.*;
import macromedia.asc.util.Context;
import macromedia.asc.util.ObjectList;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class LCBaseExtension extends AbstractTreeModificationExtension {

    private Map<String, String> modelDependenciesUnits = new HashMap<String, String>();
    private boolean liveCodingStarterAdded;

    @Override
    protected void performModifications(CompilationUnit unit) {
        if (Fcsh.livecodingBaseModeSecondPass) {
            loadSyntaxTrees();
        } else {
            saveSyntaxTree(unit);
            return;
        }

        ClassDefinitionNode classDefinitionNode = TreeNavigator.getClassDefinition(unit);

        String packageName = classDefinitionNode.pkgdef.name.id.pkg_part;
        if (!modelDependenciesUnits.keySet().contains(packageName) && !ProvidedPackages.isProvidedPackage(packageName)) {
            String mdClassName = addModelDependenciesUnit(packageName, classDefinitionNode.cx);
            modelDependenciesUnits.put(packageName, mdClassName);
        }

        if (LiveCodingUtil.hasLiveAnnotation(classDefinitionNode)) {
            TreeUtil.addImport(unit, "codeOrchestra.actionScript.liveCoding.util", "LiveCodeRegistry");
            TreeUtil.addImport(unit, "codeOrchestra.actionScript.liveCoding.util", "MethodUpdateEvent");

            for (FunctionDefinitionNode methodDefinition : TreeNavigator.getMethodDefinitions(classDefinitionNode)) {
                if (LiveCodingUtil.hasLiveAnnotation(methodDefinition)) {
                    extractMethodToLiveCodingClass(methodDefinition, classDefinitionNode);
                }
            }

            /*
               private static var __modelDependencies : ModelDependencies_com_example  = new ModelDependencies_com_example() ;
            */
            AttributeListNode attrs = new AttributeListNode(TreeUtil.createPrivateModifier(), -1);
            attrs.items.add(TreeUtil.createStaticModifier());
            TypedIdentifierNode variable = new TypedIdentifierNode(
                    new QualifiedIdentifierNode(attrs, "__modelDependencies", -1),
                    new TypeExpressionNode(TreeUtil.createIdentifier(modelDependenciesUnits.get(packageName)) ,true, false),
                    -1
            );
            CallExpressionNode selector = new CallExpressionNode(new IdentifierNode(modelDependenciesUnits.get(packageName), -1), null);
            selector.is_new = true;
            MemberExpressionNode initializer = new MemberExpressionNode(null, selector, -1);
            ListNode listNode = new ListNode(
                    null,
                    new VariableBindingNode(classDefinitionNode.pkgdef, attrs, Tokens.VAR_TOKEN, variable, initializer),
                    -1
            );
            classDefinitionNode.statements.items.add(new VariableDefinitionNode(classDefinitionNode.pkgdef, attrs, Tokens.VAR_TOKEN, listNode, -1));
        }
    }

    private void extractMethodToLiveCodingClass(FunctionDefinitionNode functionDefinitionNode, ClassDefinitionNode classDefinitionNode) {
        ObjectList<Node> oldBody = functionDefinitionNode.fexpr.body.items;
        functionDefinitionNode.fexpr.body.items = new ObjectList<Node>();

        fillStubMethodBody(functionDefinitionNode, classDefinitionNode.name.name);
        addLiveCodingClass(classDefinitionNode.name.name, functionDefinitionNode, oldBody, false);
        addListener(functionDefinitionNode, classDefinitionNode);
    }

    private void fillStubMethodBody(FunctionDefinitionNode functionDefinitionNode, String className) {
        boolean staticMethod = TreeNavigator.isStaticMethod(functionDefinitionNode);
        boolean isVoid = functionDefinitionNode.fexpr.signature.result == null;
        ObjectList<Node> newBody = functionDefinitionNode.fexpr.body.items;
        PackageDefinitionNode pkgdef = functionDefinitionNode.pkgdef;

        /*
            var method : Class  = LiveCodeRegistry.getInstance().getMethod("com.example.Main.foo");
            or
            var method : *  = LiveCodeRegistry.getInstance().getMethod("com.example.Main.static.getColor");
         */

        TypeExpressionNode methodType = staticMethod ? null : new TypeExpressionNode(
                TreeUtil.createIdentifier("Class"),
                true,
                false
        );
        TypedIdentifierNode variable = new TypedIdentifierNode(
                new QualifiedIdentifierNode(null, "method", -1),
                methodType,
                -1
        );
        String remoteMethodName = pkgdef.name.id.pkg_part + "." + className + "." + functionDefinitionNode.name.identifier.name;
        MemberExpressionNode initializer = new MemberExpressionNode(
                TreeUtil.createCall("LiveCodeRegistry", "getInstance", null),
                new CallExpressionNode(
                        new IdentifierNode("getMethod", -1),
                        new ArgumentListNode(new LiteralStringNode(remoteMethodName),-1)
                ),
                -1
        );
        ListNode listNode = new ListNode(
                null,
                new VariableBindingNode(pkgdef, null, Tokens.VAR_TOKEN, variable, initializer),
                -1
        );
        newBody.add(new VariableDefinitionNode(pkgdef, null, Tokens.VAR_TOKEN, listNode, -1));

        /*
            TODO: Test block
            new LiveMethod_com_example_Main_foo();
         */
//        ArgumentListNode args = null;
//        if (!staticMethod) {
//            args = new ArgumentListNode(new ThisExpressionNode(),-1);
//        }
//        CallExpressionNode testCall = new CallExpressionNode(new IdentifierNode(LiveCodingUtil.constructLiveCodingClassName(functionDefinitionNode, className), -1), args);
//        testCall.is_new = true;
//        newBody.add(new ExpressionStatementNode(new ListNode(null, new MemberExpressionNode(null, testCall, -1), -1)));

        /*
            [return] (new method(this)).run();
            or
            [return] method.run();
         */
        ListNode expr;
        if (!staticMethod) {
            CallExpressionNode callExpressionNode = new CallExpressionNode(
                    new IdentifierNode("method", -1),
                    new ArgumentListNode(new ThisExpressionNode(),-1)
            );
            callExpressionNode.is_new = true;
            ListNode base = new ListNode(null, new MemberExpressionNode(null, callExpressionNode, -1), -1);
            CallExpressionNode selector = new CallExpressionNode(new IdentifierNode("run", -1), null);
            expr = new ListNode(null, new MemberExpressionNode(base, selector, -1), -1);
        } else {
            expr = new ListNode(null, TreeUtil.createCall("method", "run", null), -1);
        }
        if (isVoid) {
            newBody.add(new ExpressionStatementNode(expr));
        } else {
            newBody.add(new ReturnStatementNode(expr));
        }

        /*
            return;
         */
        newBody.add(new ReturnStatementNode(null));
    }

    private void addListener(FunctionDefinitionNode functionDefinitionNode, ClassDefinitionNode classDefinitionNode) {
        /*
            public function foo_codeUpdateListener4703382380319456072 ( e : MethodUpdateEvent ) : void {
                if ( e.classFqn == "com.example.Main" ) {
                    foo();
                }
            }
         */
        boolean staticMethod = TreeNavigator.isStaticMethod(functionDefinitionNode);
        String id = UUID.randomUUID().toString(); // TODO: Just any unique id?
        String methodName = functionDefinitionNode.name.identifier.name;
        String listenerName = methodName + "_codeUpdateListener" + id;
        MethodCONode listener = new MethodCONode(listenerName, null, classDefinitionNode.cx);
        listener.addParameter("e", "MethodUpdateEvent");
        String className = classDefinitionNode.name.name;
        ListNode condition = new ListNode(
                null,
                new BinaryExpressionNode(
                        Tokens.EQUALS_TOKEN,
                        TreeUtil.createIdentifier("e", "classFqn"),
                        new LiteralStringNode(functionDefinitionNode.pkgdef.name.id.pkg_part + "." + className)
                ),
                -1
        );
        StatementListNode thenactions = new StatementListNode(new ExpressionStatementNode(new ListNode(
                null,
                TreeUtil.createCall(null, methodName, null),
                -1
        )));
        listener.statements.add(new IfStatementNode(condition, thenactions, null));
        listener.statements.add(new ReturnStatementNode(null));
        listener.isStatic = staticMethod;
        classDefinitionNode.statements.items.add(listener.getFunctionDefinitionNode());

        /*
            LiveCodeRegistry.getInstance().addEventListener(MethodUpdateEvent.METHOD_UPDATE, this.foo_codeUpdateListener4703382380319456072, false, 0, true);
            or
            LiveCodeRegistry.getInstance().addEventListener(MethodUpdateEvent.METHOD_UPDATE, Main.getColor_codeUpdateListener2123954341648648741, false, 0, true);
         */
        FunctionDefinitionNode constructorDefinition = TreeNavigator.getConstructorDefinition(classDefinitionNode);
        ArgumentListNode args = new ArgumentListNode(TreeUtil.createIdentifier("MethodUpdateEvent", "METHOD_UPDATE"), -1);
        MemberExpressionNode qListenerName = staticMethod ? TreeUtil.createIdentifier(className, listenerName) : TreeUtil.createThisIdentifier(listenerName);
        args.items.add(qListenerName);
        args.items.add(new LiteralBooleanNode(false));
        args.items.add(new LiteralNumberNode("0"));
        args.items.add(new LiteralBooleanNode(true));
        CallExpressionNode selector = new CallExpressionNode(new IdentifierNode("addEventListener", -1), args);
        MemberExpressionNode item = new MemberExpressionNode(TreeUtil.createCall("LiveCodeRegistry", "getInstance", null), selector, -1);
        ExpressionStatementNode listenerAddExpressionStatement = new ExpressionStatementNode(new ListNode(null, item, -1));
        ObjectList<Node> constructorBody = constructorDefinition.fexpr.body.items;
        constructorBody.add(constructorBody.size() - 1, listenerAddExpressionStatement);
    }

    private String addModelDependenciesUnit(String packageName, Context cx) {
        String className = "ModelDependencies_" + packageName.replaceAll("\\.", "_");
        ClassCONode classCONode = new ClassCONode(packageName, className, cx);

        MethodCONode constructor = new MethodCONode(className, null, cx);

        for (String name : projectNavigator.getClassNames(packageName)) {
            constructor.statements.add(new ExpressionStatementNode(new ListNode(null, TreeUtil.createIdentifier(name), -1)));
        }
        if (!liveCodingStarterAdded) {
            constructor.statements.add(new ExpressionStatementNode(new ListNode(null, TreeUtil.createIdentifier("LiveCodingSessionStarter", "prototype"), -1)));
            addLiveCodingStarterUnit(packageName, cx);
            liveCodingStarterAdded = true;
        }
        for (String name : projectNavigator.getLiveCodingClassNames(packageName)) {
            constructor.statements.add(new ExpressionStatementNode(new ListNode(null, TreeUtil.createIdentifier(name, "prototype"), -1)));
        }
        for (String name : ProvidedPackages.getClassNames()) {
            CallExpressionNode node = new CallExpressionNode(new IdentifierNode(name, -1), null);
            node.is_new = true;
            constructor.statements.add(new ExpressionStatementNode(new ListNode(null, new MemberExpressionNode(null, node, -1), -1)));
        }
        /* TODO: These imports does not help to resolve ModelDependencies classes, IDUNNO why. But we can skip it:

           If model A depends on model B, then there exists at least one class in A that refers to a class in B.
           Also that class in B refers to ModelDependencies_B. So ModelDependencies_B will surely get into project,
           even without direct reference in ModelDependencies_A

        for (String depName : projectNavigator.getModelDependencies(packageName)) {
            String depClassName = "ModelDependencies_" + depName.replaceAll("\\.", "_");
            CallExpressionNode node = new CallExpressionNode(new IdentifierNode(depClassName, -1), null);
            node.is_new = true;
            constructor.statements.add(new ExpressionStatementNode(new ListNode(null, new MemberExpressionNode(null, node, -1), -1)));
            classCONode.addImport(depName, depClassName);
        }
        */
        constructor.statements.add(new ReturnStatementNode(null));
        classCONode.methods.add(constructor);

        for (String name : ProvidedPackages.packages) {
            int lastDot = name.lastIndexOf(".");
            String importClassName = name.substring(lastDot + 1);
            String importPackageName = name.substring(0, lastDot);
            classCONode.addImport(importPackageName, importClassName);
        }

        classCONode.addToProject();
        return className;
    }

    private void addLiveCodingStarterUnit(String packageName, Context cx) {
        ClassCONode classCONode = new ClassCONode(packageName, "LiveCodingSessionStarter", cx);
        classCONode.staticInitializer.add(new ExpressionStatementNode(new ListNode(
                null,
                TreeUtil.createCall(
                        "LiveCodingCodeFlowUtil",
                        "setMaxLoopCount",
                        new ArgumentListNode(new LiteralNumberNode("10000"), -1)
                ),
                -1
        )));
        classCONode.staticInitializer.add(new ExpressionStatementNode(new ListNode(
                null,
                TreeUtil.createCall(
                        "LiveCodingCodeFlowUtil",
                        "setMaxRecursionCount",
                        new ArgumentListNode(new LiteralNumberNode("100"), -1)
                ),
                -1
        )));
        classCONode.staticInitializer.add(new ExpressionStatementNode(new ListNode(
                null,
                new MemberExpressionNode(
                        TreeUtil.createCall("LiveCodeRegistry", "getInstance", null),
                        new CallExpressionNode(
                                new IdentifierNode("initSession", -1),
                                new ArgumentListNode(new LiteralStringNode("123456"), -1) // TODO: randomize
                        ),
                        -1
                ),
                -1
        )));
        classCONode.addImport("codeOrchestra.actionScript.liveCoding.util", "LiveCodingCodeFlowUtil");
        classCONode.addImport("codeOrchestra.actionScript.liveCoding.util", "LiveCodeRegistry");
        classCONode.addToProject();
    }
}
