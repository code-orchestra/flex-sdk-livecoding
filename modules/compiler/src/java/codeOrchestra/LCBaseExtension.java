package codeOrchestra;

import codeOrchestra.digest.*;
import codeOrchestra.tree.*;
import codeOrchestra.util.StringUtils;
import flex2.compiler.CompilationUnit;
import flex2.compiler.common.CompilerConfiguration;
import flex2.tools.Fcsh;
import macromedia.asc.parser.*;
import macromedia.asc.util.Context;

import java.util.*;

/**
 * @author Anton.I.Neverov
 * @author Alexander Eliseyev
 */
public class LCBaseExtension extends AbstractTreeModificationExtension {

    private final CompilerConfiguration compilerConfig;
    private Map<String, String> modelDependenciesUnits = new HashMap<String, String>();
    private boolean liveCodingStarterAdded;

    public LCBaseExtension(CompilerConfiguration compilerConfig) {
        this.compilerConfig = compilerConfig;
    }

    @Override
    protected void performModifications(CompilationUnit unit) {
        ClassDefinitionNode classDefinitionNode = TreeNavigator.getClassDefinition(unit);
        if (classDefinitionNode == null) {
            Transformations.processToplevelNamespace(unit);
            return;
        }

        if (Fcsh.livecodingBaseModeSecondPass) {
            loadSyntaxTrees();
        } else {
            DigestManager.getInstance().addToDigestUnresolved(classDefinitionNode);
            saveSyntaxTree(unit);
            return;
        }

        String packageName = classDefinitionNode.pkgdef.name.id.pkg_part;
        String className = classDefinitionNode.name.name;
        String classFqName = StringUtils.longNameFromNamespaceAndShortName(packageName, className);

        if (!modelDependenciesUnits.keySet().contains(packageName) && !ProvidedPackages.isProvidedPackage(packageName)) {
            String mdClassName = addModelDependenciesUnit(packageName, classDefinitionNode.cx);
            modelDependenciesUnits.put(packageName, mdClassName);
        }

        if (LiveCodingUtil.canBeUsedForLiveCoding(classDefinitionNode)) {
            if (!TreeNavigator.isDynamic(classDefinitionNode)) {
                classDefinitionNode.attrs.items.add(TreeUtil.createDynamicModifier());
            }

            TreeUtil.addImport(unit, "codeOrchestra.actionScript.liveCoding.util", "LiveCodeRegistry");
            TreeUtil.addImport(unit, "codeOrchestra.actionScript.liveCoding.util", "MethodUpdateEvent");

            List<Node> staticListenerAddStatements = new ArrayList<Node>();
            for (FunctionDefinitionNode methodDefinition : TreeNavigator.getMethodDefinitions(classDefinitionNode)) {
                if (LiveCodingUtil.canBeUsedForLiveCoding(methodDefinition)) {
                    extractMethodToLiveCodingClass(methodDefinition, classDefinitionNode, staticListenerAddStatements);
                }

                // COLT-34
                if (TreeNavigator.isOverridingMethod(methodDefinition)) {
                    processOverridingMethod(methodDefinition, classDefinitionNode);
                }
            }

            // Add live initializer method
            addLiveInitializerMethod(classDefinitionNode, false, staticListenerAddStatements);

            // COLT-34
            Set<IMember> visibleInstanceProtectedMembers = DigestManager.getInstance().getVisibleInstanceProtectedMembers(classFqName);
            for (IMember protectedMember : visibleInstanceProtectedMembers) {
                MemberKind kind = protectedMember.getKind();
                if (kind == MemberKind.FIELD) {
                    processProtectedField(protectedMember, classDefinitionNode);
                } else if (EnumSet.of(MemberKind.GETTER, MemberKind.SETTER, MemberKind.METHOD).contains(kind)) {
                    processProtectedMethod(protectedMember, classDefinitionNode);
                }
            }

            makeMembersPublic(classDefinitionNode);

            // COLT-220 - Add instance assets listeners
            List<VariableDefinitionNode> embedFields = TreeNavigator.getFieldDefinitionsWithAnnotation(classDefinitionNode, "Embed");
            Transformations.addAssetListeners(unit, classDefinitionNode, embedFields, false);

            // Extract all internal classes
            List<String> internalClassesNames = new ArrayList<String>();
            ProgramNode syntaxTree = (ProgramNode) unit.getSyntaxTree();
            for (ClassDefinitionNode internalClass : TreeNavigator.getInternalClassDefinitions(syntaxTree)) {
                Transformations.extractInternalClass(unit, classDefinitionNode, packageName, internalClassesNames, syntaxTree, internalClass);
            }

            /*
               private static var __modelDependencies : ModelDependencies_com_example  = new ModelDependencies_com_example() ;
            */
            AttributeListNode attrs = new AttributeListNode(TreeUtil.createPrivateModifier(), -1);
            attrs.items.add(TreeUtil.createStaticModifier());
            TypedIdentifierNode variable = new TypedIdentifierNode(
                    new QualifiedIdentifierNode(attrs, "__modelDependencies", -1),
                    new TypeExpressionNode(TreeUtil.createIdentifier(modelDependenciesUnits.get(packageName)), true, false),
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

            // COLT-73
            String initMethodName = "initLiveMethod_" + className;
            MethodCONode initMethod = new MethodCONode(initMethodName, "int", classDefinitionNode.cx);
            initMethod.isStatic = true;
            FunctionDefinitionNode initMethodNode = initMethod.getFunctionDefinitionNode();
            initMethodNode.pkgdef = classDefinitionNode.pkgdef;
            // COLT-67
            for (String ownLiveMethodClass : projectNavigator.getLiveCodingClassNames(packageName, className)) {
                initMethodNode.fexpr.body.items.add(new ExpressionStatementNode(new ListNode(null, TreeUtil.createIdentifier(ownLiveMethodClass, "prototype"), -1)));
            }
            for (String internalClassName : internalClassesNames) {
                initMethodNode.fexpr.body.items.add(new ExpressionStatementNode(new ListNode(null, TreeUtil.createIdentifier(internalClassName, "prototype"), -1)));
            }
            // COLT-220
            for (Node staticListenerAddStatement : staticListenerAddStatements) {
                initMethodNode.fexpr.body.items.add(staticListenerAddStatement);
            }
            Node assetListenerAddStatement = Transformations.addAssetListeners(unit, classDefinitionNode, embedFields, true);
            if (assetListenerAddStatement != null) {
                initMethodNode.fexpr.body.items.add(assetListenerAddStatement);
            }
            if (!liveCodingStarterAdded) {
                initMethodNode.fexpr.body.items.add(new ExpressionStatementNode(new ListNode(null, TreeUtil.createIdentifier("LiveCodingSessionStarter", "prototype"), -1)));
                addLiveCodingStarterUnit(packageName, classDefinitionNode.cx);
                liveCodingStarterAdded = true;
            }
            initMethodNode.fexpr.body.items.add(new ReturnStatementNode(new LiteralNumberNode("0")));
            classDefinitionNode.statements.items.add(initMethodNode);
            FieldCONode initField = new FieldCONode("initLiveField_" + className, "int");
            initField.isStatic = true;
            initField.initializer = TreeUtil.createCall(null, initMethodName, null);
            VariableDefinitionNode variableDefinitionNode = initField.getVariableDefinitionNode();
            variableDefinitionNode.pkgdef = classDefinitionNode.pkgdef;
            classDefinitionNode.statements.items.add(0, variableDefinitionNode);
        }
    }

    private void processProtectedField(IMember member, ClassDefinitionNode classDefinitionNode) {
        int inheritanceLevel = DigestManager.getInstance().getInheritanceLevel(TreeUtil.getFqName(classDefinitionNode));

        String fieldName = member.getName();
        String accessorName = fieldName + "_protected" + inheritanceLevel;

        // Setter
        MethodCONode setter = new MethodCONode(accessorName, null, classDefinitionNode.cx);
        FunctionDefinitionNode setterNode = setter.getFunctionDefinitionNode();
        ParameterNode parameterNode = new ParameterNode(
                Tokens.VAR_TOKEN,
                new IdentifierNode(fieldName, -1),
                TreeUtil.getType(member.getType()),
                null
        );
        setterNode.fexpr.signature.parameter = new ParameterListNode(null, parameterNode, -1);
        setterNode.name.kind = Tokens.SET_TOKEN;
        setterNode.skipLiveCoding = true;
        setterNode.pkgdef = classDefinitionNode.pkgdef;
        setterNode.fexpr.internal_name = fieldName + "$0";

        StatementListNode setterBody = setterNode.fexpr.body;
        setterBody.items.add(new ExpressionStatementNode(new ListNode(null,
                new MemberExpressionNode(new ThisExpressionNode(),
                        new SetExpressionNode(
                                new IdentifierNode(fieldName, -1),
                                new ArgumentListNode(TreeUtil.createIdentifier(fieldName), -1)
                        )
                        , -1), -1)));
        setterBody.items.add(new ReturnStatementNode(null));
        classDefinitionNode.statements.items.add(setterNode);

        // Getter
        MethodCONode getter = new MethodCONode(accessorName, null, classDefinitionNode.cx);
        FunctionDefinitionNode getterNode = getter.getFunctionDefinitionNode();
        getterNode.name.kind = Tokens.GET_TOKEN;
        getterNode.skipLiveCoding = true;
        getterNode.pkgdef = classDefinitionNode.pkgdef;
        getterNode.fexpr.internal_name = fieldName + "$1";
        getterNode.fexpr.signature.result = TreeUtil.getType(member.getType());

        StatementListNode getterBody = getterNode.fexpr.body;
        getterBody.items.add(new ReturnStatementNode(new MemberExpressionNode(new ThisExpressionNode(), new GetExpressionNode(TreeUtil.createIdentifier(fieldName)), -1)));
        classDefinitionNode.statements.items.add(getterNode);
    }

    private void processProtectedMethod(IMember member, ClassDefinitionNode classDefinitionNode) {
        boolean isVoid = "void".equals(member.getType());

        int inheritanceLevel = DigestManager.getInstance().getInheritanceLevel(TreeUtil.getFqName(classDefinitionNode));
        String functionName = member.getName();
        String accessorName = functionName + "_protected" + inheritanceLevel;
        MethodCONode protectedAccessor = new MethodCONode(accessorName, null, classDefinitionNode.cx);

        FunctionDefinitionNode accessorFunctionDefinitionNode = protectedAccessor.getFunctionDefinitionNode();
        accessorFunctionDefinitionNode.skipLiveCoding = true;
        accessorFunctionDefinitionNode.fexpr.needsArguments = 0x1;
        accessorFunctionDefinitionNode.pkgdef = classDefinitionNode.pkgdef;

        if (member.getKind() == MemberKind.GETTER) {
            accessorFunctionDefinitionNode.name.kind = Tokens.GET_TOKEN;
        } else if (member.getKind() == MemberKind.SETTER) {
            accessorFunctionDefinitionNode.name.kind = Tokens.SET_TOKEN;
        }
        accessorFunctionDefinitionNode.fexpr.internal_name = accessorName + "$" + accessorFunctionDefinitionNode.name.kind;

        accessorFunctionDefinitionNode.fexpr.signature.result = TreeUtil.getType(member.getType());
        if (!member.getParameters().isEmpty()) {
            IParameter firstParameter = member.getParameters().get(0);
            accessorFunctionDefinitionNode.fexpr.signature.parameter = new ParameterListNode(
                    null,
                    new ParameterNode(Tokens.VAR_TOKEN,
                            new IdentifierNode(firstParameter.getName(), -1),
                            TreeUtil.getType(firstParameter.getType()),
                            null),
                    -1);

            for (int i = 1; i < member.getParameters().size(); i++) {
                IParameter parameter = member.getParameters().get(i);
                accessorFunctionDefinitionNode.fexpr.signature.parameter.items.add(new ParameterNode(Tokens.VAR_TOKEN,
                        new IdentifierNode(parameter.getName(), -1),
                        TreeUtil.getType(parameter.getType()),
                        null));
            }
        }

        /*
         this.someProtectedMethod.apply(this, arguments);
        */
        StatementListNode newBody = accessorFunctionDefinitionNode.fexpr.body;

        if (member.getKind() == MemberKind.METHOD) {
            ArgumentListNode args = new ArgumentListNode(new ThisExpressionNode(), -1);
            args.items.add(TreeUtil.createIdentifier("arguments".intern()));
            Node expr = new ListNode(null, new MemberExpressionNode(
                    new MemberExpressionNode(new ThisExpressionNode(), new GetExpressionNode(TreeUtil.createIdentifier(functionName)), -1),
                    new CallExpressionNode(
                            new IdentifierNode("apply", -1),
                            args
                    ),
                    -1
            ), -1);
            if (isVoid) {
                newBody.items.add(new ExpressionStatementNode(expr));
                newBody.items.add(new ReturnStatementNode(null));
            } else {
                newBody.items.add(new ReturnStatementNode(expr));
            }
        } else if (member.getKind() == MemberKind.SETTER) {
            newBody.items.add(new ExpressionStatementNode(
                    new ListNode(null, new MemberExpressionNode(new ThisExpressionNode(), new SetExpressionNode(TreeUtil.createIdentifier(functionName), new ArgumentListNode(TreeUtil.createIdentifier(member.getParameters().get(0).getName()), -1)), -1), -1)
            ));
            newBody.items.add(new ReturnStatementNode(null));
        } else if (member.getKind() == MemberKind.GETTER) {
            newBody.items.add(new ReturnStatementNode(TreeUtil.createIdentifier(functionName)));
        } else {
            throw new IllegalArgumentException("Unknown function kind: " + member.getKind());
        }

        classDefinitionNode.statements.items.add(accessorFunctionDefinitionNode);
    }

    private void processOverridingMethod(FunctionDefinitionNode functionDefinitionNode, ClassDefinitionNode classDefinitionNode) {
        if (TreeNavigator.isStaticMethod(functionDefinitionNode)) {
            return;
        }

        boolean isVoid = functionDefinitionNode.fexpr.signature.result == null;
        int inheritanceLevel = DigestManager.getInstance().getInheritanceLevel(TreeUtil.getFqName(classDefinitionNode));
        String functionName = functionDefinitionNode.name.identifier.name;
        String accessorName = functionName + "_overriden_super" + inheritanceLevel;
        MethodCONode protectedAccessor = new MethodCONode(accessorName, null, classDefinitionNode.cx);

        FunctionDefinitionNode accessorFunctionDefinitionNode = protectedAccessor.getFunctionDefinitionNode();
        accessorFunctionDefinitionNode.skipLiveCoding = true;
        accessorFunctionDefinitionNode.fexpr.needsArguments = 0x1;
        accessorFunctionDefinitionNode.pkgdef = functionDefinitionNode.pkgdef;
        accessorFunctionDefinitionNode.name.kind = functionDefinitionNode.name.kind;
        accessorFunctionDefinitionNode.fexpr.internal_name = accessorName + "$" + accessorFunctionDefinitionNode.name.kind;

        try {
            accessorFunctionDefinitionNode.fexpr.signature = (FunctionSignatureNode) functionDefinitionNode.fexpr.signature.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        StatementListNode newBody = accessorFunctionDefinitionNode.fexpr.body;

        if (accessorFunctionDefinitionNode.name.kind == Tokens.GET_TOKEN) {
            newBody.items.add(new ReturnStatementNode(new MemberExpressionNode(new SuperExpressionNode(null), new GetExpressionNode(new IdentifierNode(functionName, -1)), -1)));
        } else if (accessorFunctionDefinitionNode.name.kind == Tokens.SET_TOKEN) {
            String parameterName = accessorFunctionDefinitionNode.fexpr.signature.parameter.items.get(0).identifier.name;
            newBody.items.add(new ExpressionStatementNode(
                    new ListNode(null, new MemberExpressionNode(new SuperExpressionNode(null), new SetExpressionNode(TreeUtil.createIdentifier(functionName), new ArgumentListNode(TreeUtil.createIdentifier(parameterName), -1)), -1), -1)
            ));
            newBody.items.add(new ReturnStatementNode(null));
        } else {
            /*
            super.someProtectedMethod.apply(this, arguments);
            */
            ArgumentListNode args = new ArgumentListNode(new ThisExpressionNode(), -1);
            args.items.add(TreeUtil.createIdentifier("arguments".intern()));
            Node expr = new ListNode(null, new MemberExpressionNode(
                    new MemberExpressionNode(new SuperExpressionNode(null), new GetExpressionNode(TreeUtil.createIdentifier(functionName)), -1),
                    new CallExpressionNode(
                            new IdentifierNode("apply", -1),
                            args
                    ),
                    -1
            ), -1);
            if (isVoid) {
                newBody.items.add(new ExpressionStatementNode(expr));
                newBody.items.add(new ReturnStatementNode(null));
            } else {
                newBody.items.add(new ReturnStatementNode(expr));
            }
        }

        classDefinitionNode.statements.items.add(accessorFunctionDefinitionNode);
    }

    private void makeMembersPublic(ClassDefinitionNode classDefinitionNode) {
        // Fields
        for (VariableDefinitionNode variableDefinitionNode : TreeNavigator.getFieldDefinitions(classDefinitionNode)) {
            TreeUtil.makePublic(variableDefinitionNode.attrs, false);

            // Make vars out of constants
            boolean hasEmbed = TreeNavigator.getAnnotation(variableDefinitionNode, "Embed") != null;
            if (hasEmbed) {
                if (variableDefinitionNode.kind == Tokens.CONST_TOKEN) {
                    variableDefinitionNode.kind = Tokens.VAR_TOKEN;
                }
                if (variableDefinitionNode.list != null && variableDefinitionNode.list.items != null) {
                    for (Node node : variableDefinitionNode.list.items) {
                        if (node instanceof VariableBindingNode) {
                            VariableBindingNode variableBindingNode = (VariableBindingNode) node;
                            if (variableBindingNode.kind == Tokens.CONST_TOKEN) {
                                variableBindingNode.kind = Tokens.VAR_TOKEN;
                            }
                        }
                    }
                }
            }
        }

        // Methods
        for (FunctionDefinitionNode functionDefinitionNode : TreeNavigator.getMethodDefinitions(classDefinitionNode)) {
            if (EnumSet.of(Visibility.PRIVATE, Visibility.INTERNAL).contains(TreeNavigator.getVisibility(functionDefinitionNode)) && DigestManager.getInstance().isOverriden(functionDefinitionNode, TreeUtil.getFqName(classDefinitionNode))) {
                classDefinitionNode.cx.localizedWarning(functionDefinitionNode.pos(), "[COLT] Can't make a private or internal function public as it would be overriden otherwise");
                continue;
            }

            TreeUtil.makePublic(functionDefinitionNode.attrs, TreeNavigator.isStaticMethod(functionDefinitionNode) ? false : true);
        }
    }


    private String addModelDependenciesUnit(String packageName, Context cx) {
        String className = "ModelDependencies_" + packageName.replaceAll("\\.", "_");
        ClassCONode classCONode = new ClassCONode(packageName, className, cx);

        MethodCONode constructor = new MethodCONode(className, null, cx);

        for (String name : projectNavigator.getClassNames(packageName)) {
            constructor.statements.add(new ExpressionStatementNode(new ListNode(null, TreeUtil.createIdentifier(name), -1)));
        }
        for (String name : ProvidedPackages.getClassNames()) {
            CallExpressionNode node = new CallExpressionNode(new IdentifierNode(name, -1), null);
            node.is_new = true;
            constructor.statements.add(new ExpressionStatementNode(new ListNode(null, new MemberExpressionNode(null, node, -1), -1)));
        }
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

        // COLT-41
        ArgumentListNode setSocketArgs = new ArgumentListNode(new LiteralStringNode(LiveCodingCLIParameters.getTraceHost()), -1);
        setSocketArgs.items.add(new LiteralNumberNode(String.valueOf(LiveCodingCLIParameters.getTracePort())));
        classCONode.staticInitializer.add(new ExpressionStatementNode(new ListNode(
                null,
                TreeUtil.createCall(
                        "LogUtil",
                        "setSocketAddress",
                        setSocketArgs
                ),
                -1
        )));

        classCONode.staticInitializer.add(new ExpressionStatementNode(new ListNode(
                null,
                TreeUtil.createCall(
                        "LiveCodingCodeFlowUtil",
                        "setMaxLoopCount",
                        new ArgumentListNode(new LiteralNumberNode(String.valueOf(LiveCodingCLIParameters.getMaxLoopIterations())), -1)
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
                                new ArgumentListNode(new LiteralStringNode(StringUtils.generateId()), -1)
                        ),
                        -1
                ),
                -1
        )));
        classCONode.addImport("codeOrchestra.actionScript.logging.logUtil", "LogUtil");
        classCONode.addImport("codeOrchestra.actionScript.liveCoding.util", "LiveCodingCodeFlowUtil");
        classCONode.addImport("codeOrchestra.actionScript.liveCoding.util", "LiveCodeRegistry");
        classCONode.addToProject();
    }

}
