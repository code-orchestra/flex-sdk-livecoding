package codeOrchestra;

import codeOrchestra.digest.DigestManager;
import codeOrchestra.digest.IMember;
import codeOrchestra.digest.IParameter;
import codeOrchestra.digest.MemberKind;
import codeOrchestra.tree.*;
import codeOrchestra.util.StringUtils;
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
        ClassDefinitionNode classDefinitionNode = TreeNavigator.getClassDefinition(unit);
        if (classDefinitionNode == null) {
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

            for (FunctionDefinitionNode methodDefinition : TreeNavigator.getMethodDefinitions(classDefinitionNode)) {
                if (LiveCodingUtil.canBeUsedForLiveCoding(methodDefinition)) {
                    extractMethodToLiveCodingClass(methodDefinition, classDefinitionNode);
                }

                // COLT-34
                if (TreeNavigator.isOverridingMethod(methodDefinition)) {
                    processOverridingMethod(methodDefinition, classDefinitionNode);
                }
            }
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

            // COLT-95 - Add asset listeners
            List<VariableDefinitionNode> embedFields = TreeNavigator.getFieldDefinitionsWithAnnotation(classDefinitionNode, "Embed");
            if (!embedFields.isEmpty()) {
                TreeUtil.addImport(unit, "codeOrchestra.actionScript.liveCoding.util", "AssetUpdateEvent");

                MethodCONode assetsUpdateListener = new MethodCONode("assetsUpdateListener" + generateId(), null, classDefinitionNode.cx);
                assetsUpdateListener.addParameter("event", "AssetUpdateEvent");
                FunctionDefinitionNode assetsBroadcastMethod = assetsUpdateListener.getFunctionDefinitionNode();
                assetsBroadcastMethod.pkgdef = classDefinitionNode.pkgdef;

                for (VariableDefinitionNode variableDefinitionNode : embedFields) {
                    MetaDataNode embed = LiveCodingUtil.getAnnotation(variableDefinitionNode, "Embed");
                    if (embed == null) {
                        continue;
                    }

                    String source = embed.getValue("source");
                    if (StringUtils.isEmpty(source)) {
                        continue;
                    }

                    VariableBindingNode var
                            = (VariableBindingNode) variableDefinitionNode.list.items.get(0);

                    String prefix;
                    if (!StringUtils.isEmpty(packageName)) {
                        prefix = StringUtils.concatenate("../", packageName.split("[.]").length);
                    } else {
                        prefix = "";
                    }
                    ListNode condition = new ListNode(
                            null,
                            new BinaryExpressionNode(
                                    Tokens.EQUALS_TOKEN,
                                    new BinaryExpressionNode(Tokens.PLUS_TOKEN, new LiteralStringNode(prefix), TreeUtil.createIdentifier("event", "source")),
                                    new LiteralStringNode(source)
                            ),
                            -1
                    );
                    String embedFieldName = var.variable.identifier.name;
                    StatementListNode thenActions = new StatementListNode(new ExpressionStatementNode(new ListNode(
                            null,
                            new ExpressionStatementNode(
                                    new ListNode(null, new MemberExpressionNode(new ThisExpressionNode(), new SetExpressionNode(TreeUtil.createIdentifier(embedFieldName), new ArgumentListNode(TreeUtil.createIdentifier("event", "assetClass"), -1)),
                            -1), -1
                    )), -1)));


                    // Call listeners
                    List<FunctionDefinitionNode> liveAssetUpdateListeners = TreeNavigator.getMethodDefinitionsWithAnnotation(classDefinitionNode, "LiveAssetUpdateListener");
                    for (FunctionDefinitionNode liveAssetUpdateListener : liveAssetUpdateListeners) {
                        MetaDataNode liveAssetUpdateListenerAnnotation = LiveCodingUtil.getAnnotation(liveAssetUpdateListener, "LiveAssetUpdateListener");
                        String sourceParam = liveAssetUpdateListenerAnnotation.getValue("source");
                        String fieldParam = liveAssetUpdateListenerAnnotation.getValue("field");

                        boolean validField = (!StringUtils.isEmpty(embedFieldName)) && fieldParam.equals(embedFieldName);
                        boolean noParams = liveAssetUpdateListenerAnnotation.getValues() == null || liveAssetUpdateListenerAnnotation.getValues().length == 0;
                        boolean validSource = source.equals(sourceParam);

                        if (noParams || validField || validSource) {
                            thenActions.items.add(new ExpressionStatementNode(new ListNode(null, TreeUtil.createCall(null, liveAssetUpdateListener.fexpr.identifier.name, null), -1)));
                        }
                    }

                    thenActions.items.add(new ReturnStatementNode(null));
                    IfStatementNode ifStatementNode = new IfStatementNode(condition, thenActions, null);
                    assetsBroadcastMethod.fexpr.body.items.add(ifStatementNode);
                }

                assetsBroadcastMethod.fexpr.body.items.add(new ReturnStatementNode(null));
                classDefinitionNode.statements.items.add(assetsBroadcastMethod);

                /*
                    LiveCodeRegistry.getInstance().addEventListener(AssetUpdateEvent.ASSET_UPDATE, this.assetsUpdateListener7055724444913929522, false, 0, true);
                 */

                FunctionDefinitionNode constructorDefinition = TreeNavigator.getConstructorDefinition(classDefinitionNode);
                ArgumentListNode args = new ArgumentListNode(TreeUtil.createIdentifier("AssetUpdateEvent", "ASSET_UPDATE"), -1);
                MemberExpressionNode qListenerName = TreeUtil.createThisIdentifier(assetsUpdateListener.methodName);
                args.items.add(qListenerName);
                args.items.add(new LiteralBooleanNode(false));
                args.items.add(new LiteralNumberNode(String.valueOf("0")));
                args.items.add(new LiteralBooleanNode(true));
                CallExpressionNode selector = new CallExpressionNode(new IdentifierNode("addEventListener", -1), args);
                MemberExpressionNode item = new MemberExpressionNode(TreeUtil.createCall("LiveCodeRegistry", "getInstance", null), selector, -1);
                ExpressionStatementNode listenerAddExpressionStatement = new ExpressionStatementNode(new ListNode(null, item, -1));
                if (constructorDefinition == null) {
                    MethodCONode constructorRegularNode = new MethodCONode(classDefinitionNode.name.name, null, classDefinitionNode.cx);
                    constructorDefinition = constructorRegularNode.getFunctionDefinitionNode();
                    constructorDefinition.pkgdef = classDefinitionNode.pkgdef;
                    constructorDefinition.fexpr.body.items.add(new ReturnStatementNode(null));
                }
                ObjectList<Node> constructorBody = constructorDefinition.fexpr.body.items;
                constructorBody.add(constructorBody.size() - 1, listenerAddExpressionStatement);
            }

            // Extract all internal classes
            List<String> internalClassesNames = new ArrayList<String>();
            ProgramNode syntaxTree = (ProgramNode) unit.getSyntaxTree();
            for (ClassDefinitionNode internalClass : TreeNavigator.getInternalClassDefinitions(syntaxTree)) {
                internalClassesNames.add(internalClass.name.name);

                // Detach
                syntaxTree.statements.items.remove(internalClass);

                // Make public
                internalClass.attrs = new AttributeListNode(TreeUtil.createPublicModifier(), -1);

                // Add as a separate unit
                TreeUtil.createUnitFromInternalClass(internalClass, packageName, classDefinitionNode.cx, TreeNavigator.getImports(syntaxTree), unit.inheritance);
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

    private void extractMethodToLiveCodingClass(FunctionDefinitionNode functionDefinitionNode, ClassDefinitionNode classDefinitionNode) {
        ObjectList<Node> oldBody = functionDefinitionNode.fexpr.body.items;
        functionDefinitionNode.fexpr.body.items = new ObjectList<Node>();

        String className = classDefinitionNode.name.name;
        fillStubMethodBody(functionDefinitionNode, className);

        addLiveCodingClass(className, classDefinitionNode, functionDefinitionNode, oldBody, false);

        if (LiveCodingUtil.isLiveCodeUpdateListener(functionDefinitionNode)) {
            addListener(functionDefinitionNode, classDefinitionNode);
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
        try {
            accessorFunctionDefinitionNode.fexpr.signature = (FunctionSignatureNode) functionDefinitionNode.fexpr.signature.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        StatementListNode newBody = accessorFunctionDefinitionNode.fexpr.body;

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

        classDefinitionNode.statements.items.add(accessorFunctionDefinitionNode);
    }

    private void makeMembersPublic(ClassDefinitionNode classDefinitionNode) {
        // Fields
        for (VariableDefinitionNode variableDefinitionNode : TreeNavigator.getFieldDefinitions(classDefinitionNode)) {
            TreeUtil.makePublic(variableDefinitionNode.attrs, false);
        }

        // Methods
        for (FunctionDefinitionNode functionDefinitionNode : TreeNavigator.getMethodDefinitions(classDefinitionNode)) {
            TreeUtil.makePublic(functionDefinitionNode.attrs, TreeNavigator.isStaticMethod(functionDefinitionNode) ? false : true);
        }
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
        String remoteMethodName = LiveCodingUtil.constructLiveCodingMethodId(functionDefinitionNode, className);
        MemberExpressionNode initializer = new MemberExpressionNode(
                TreeUtil.createCall("LiveCodeRegistry", "getInstance", null),
                new CallExpressionNode(
                        new IdentifierNode("getMethod", -1),
                        new ArgumentListNode(new LiteralStringNode(remoteMethodName), -1)
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

        // COLT-57
        ArgumentListNode argumentListNode = null;
        ParameterListNode parameterListNode = functionDefinitionNode.fexpr.signature.parameter;
        if (parameterListNode != null && parameterListNode.items.size() > 0) {
            argumentListNode = new ArgumentListNode(TreeUtil.createIdentifier(parameterListNode.items.get(0).identifier.name), -1);
            if (parameterListNode.items.size() > 1) {
                for (int i = 1; i < parameterListNode.items.size(); i++) {
                    argumentListNode.items.add(TreeUtil.createIdentifier(parameterListNode.items.get(i).identifier.name));
                }
            }
        }
        ListNode expr;
        if (!staticMethod) {
            CallExpressionNode callExpressionNode = new CallExpressionNode(
                    new IdentifierNode("method", -1),
                    new ArgumentListNode(new ThisExpressionNode(), -1)
            );
            callExpressionNode.is_new = true;
            ListNode base = new ListNode(null, new MemberExpressionNode(null, callExpressionNode, -1), -1);
            CallExpressionNode selector = new CallExpressionNode(new IdentifierNode("run", -1), argumentListNode);
            expr = new ListNode(null, new MemberExpressionNode(base, selector, -1), -1);
        } else {
            expr = new ListNode(null, TreeUtil.createCall("method", "run", argumentListNode), -1);
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

        String classFqn = StringUtils.longNameFromNamespaceAndShortName(functionDefinitionNode.pkgdef.name.id.pkg_part, classDefinitionNode.name.name);
        String methodNameToFilter = null;
        boolean weak = true;
        int priority = 0;

        MetaDataNode annotation = LiveCodingUtil.getAnnotation(functionDefinitionNode, "LiveCodeUpdateListener");
        if (annotation != null) {
            String annotationClassFqn = annotation.getValue("classFqn");
            if (!StringUtils.isEmpty(annotationClassFqn)) {
                classFqn = annotationClassFqn;
            }

            String annotationMethod = annotation.getValue("method");
            if (!StringUtils.isEmpty(annotationMethod)) {
                methodNameToFilter = annotationMethod;
            }

            String annotationWeak = annotation.getValue("weak");
            if (!StringUtils.isEmpty(annotationWeak)) {
                weak = Boolean.parseBoolean(annotationWeak);
            }

            String annotationPriority = annotation.getValue("priority");
            if (!StringUtils.isEmpty(annotationPriority)) {
                priority = Integer.parseInt(annotationPriority);
            }
        }

        boolean staticMethod = TreeNavigator.isStaticMethod(functionDefinitionNode);
        String id = UUID.randomUUID().toString(); // TODO: Just any unique id?
        String listenerName = functionDefinitionNode.name.identifier.name + "_codeUpdateListener" + id;
        MethodCONode listener = new MethodCONode(listenerName, null, classDefinitionNode.cx);
        listener.addParameter("e", "MethodUpdateEvent");

        Node firstStatement = null;
        MemberExpressionNode methodCall = TreeUtil.createCall(null, functionDefinitionNode.name.identifier.name, null);
        if ("*".equals(classFqn)) {
            firstStatement = new ExpressionStatementNode(new ListNode(null, methodCall, -1));
        } else {
            ListNode condition = new ListNode(
                    null,
                    new BinaryExpressionNode(
                            Tokens.EQUALS_TOKEN,
                            TreeUtil.createIdentifier("e", "classFqn"),
                            new LiteralStringNode(classFqn)
                    ),
                    -1
            );
            StatementListNode thenactions = new StatementListNode(new ExpressionStatementNode(new ListNode(
                    null,
                    methodCall,
                    -1
            )));
            firstStatement = new IfStatementNode(condition, thenactions, null);
        }

        if (!StringUtils.isEmpty(methodNameToFilter)) {
            ListNode condition = new ListNode(
                    null,
                    new BinaryExpressionNode(
                            Tokens.EQUALS_TOKEN,
                            TreeUtil.createIdentifier("e", "methodName"),
                            new LiteralStringNode(methodNameToFilter)
                    ),
                    -1
            );
            StatementListNode thenactions = new StatementListNode(new ExpressionStatementNode(new ListNode(
                    null,
                    firstStatement,
                    -1
            )));
            firstStatement = new IfStatementNode(condition, thenactions, null);
        }

        listener.statements.add(firstStatement);
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
        MemberExpressionNode qListenerName = staticMethod ? TreeUtil.createIdentifier(classDefinitionNode.name.name, listenerName) : TreeUtil.createThisIdentifier(listenerName);
        args.items.add(qListenerName);
        args.items.add(new LiteralBooleanNode(false));
        args.items.add(new LiteralNumberNode(String.valueOf(priority)));
        args.items.add(new LiteralBooleanNode(weak));
        CallExpressionNode selector = new CallExpressionNode(new IdentifierNode("addEventListener", -1), args);
        MemberExpressionNode item = new MemberExpressionNode(TreeUtil.createCall("LiveCodeRegistry", "getInstance", null), selector, -1);
        ExpressionStatementNode listenerAddExpressionStatement = new ExpressionStatementNode(new ListNode(null, item, -1));

        if (constructorDefinition == null) {
            MethodCONode constructorRegularNode = new MethodCONode(classDefinitionNode.name.name, null, classDefinitionNode.cx);
            constructorDefinition = constructorRegularNode.getFunctionDefinitionNode();
            constructorDefinition.pkgdef = classDefinitionNode.pkgdef;
            constructorDefinition.fexpr.body.items.add(new ReturnStatementNode(null));
            classDefinitionNode.statements.items.add(0, constructorDefinition);
        }

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
        // We now do it in the live classes constructor
        /*
        for (String name : projectNavigator.getLiveCodingClassNames(packageName)) {
            constructor.statements.add(new ExpressionStatementNode(new ListNode(null, TreeUtil.createIdentifier(name, "prototype"), -1)));
        }
        */
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
                                new ArgumentListNode(new LiteralStringNode(generateId()), -1)
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
