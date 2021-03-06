package codeOrchestra;

import codeOrchestra.digest.*;
import codeOrchestra.tree.*;
import codeOrchestra.util.InsertPosition;
import codeOrchestra.util.Pair;
import codeOrchestra.util.StringUtils;
import codeOrchestra.util.Triple;
import flex2.compiler.CompilationUnit;
import macromedia.asc.parser.*;
import macromedia.asc.util.ObjectList;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Alexander Eliseyev
 */
public class Transformations {

    public static void extractInternalClass(CompilationUnit unit, ClassDefinitionNode classDefinitionNode, String packageName, List<String> internalClassesNames, ProgramNode syntaxTree, ClassDefinitionNode internalClass) {
        internalClassesNames.add(internalClass.name.name);

        // Detach
        syntaxTree.statements.items.remove(internalClass);

        // Make public
        internalClass.attrs = new AttributeListNode(TreeUtil.createPublicModifier(), -1);

        // Add as a separate unit
        TreeUtil.createUnitFromInternalClass(internalClass, packageName, classDefinitionNode.cx, TreeNavigator.getImports(syntaxTree), unit.inheritance);
    }

    /**
     * @return listener add statement
     */
    public static Node addCodeUpdateListener(FunctionDefinitionNode functionDefinitionNode, ClassDefinitionNode classDefinitionNode) {
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

        MetaDataNode annotation = TreeNavigator.getAnnotation(functionDefinitionNode, "LiveCodeUpdateListener");
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

        boolean isStatic = TreeNavigator.isStaticMethod(functionDefinitionNode);
        String id = StringUtils.generateId();
        String listenerName = functionDefinitionNode.name.identifier.name + "_codeUpdateListener" + id;
        MethodCONode listener = new MethodCONode(listenerName, null, classDefinitionNode.cx);
        listener.addParameter("e", "MethodUpdateEvent");

        Node firstStatement;
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
            ListNode condition;
            if (methodNameToFilter.contains(",")) {
                String[] split = methodNameToFilter.split(",");

                BinaryExpressionNode orExpression = new BinaryExpressionNode(
                        Tokens.EQUALS_TOKEN,
                        TreeUtil.createIdentifier("e", "methodName"),
                        new LiteralStringNode(split[0].trim())
                );

                for (int i = 1; i < split.length; i++) {
                    String methodName = split[i].trim();
                    orExpression = new BinaryExpressionNode(Tokens.LOGICALOR_TOKEN, orExpression, new BinaryExpressionNode(
                            Tokens.EQUALS_TOKEN,
                            TreeUtil.createIdentifier("e", "methodName"),
                            new LiteralStringNode(methodName)
                    ));
                }

                condition = new ListNode(
                        null,
                        orExpression,
                        -1
                );
            } else {
                condition = new ListNode(
                        null,
                        new BinaryExpressionNode(
                                Tokens.EQUALS_TOKEN,
                                TreeUtil.createIdentifier("e", "methodName"),
                                new LiteralStringNode(methodNameToFilter)
                        ),
                        -1
                );
            }
            StatementListNode thenactions = new StatementListNode(new ExpressionStatementNode(new ListNode(
                    null,
                    firstStatement,
                    -1
            )));
            firstStatement = new IfStatementNode(condition, thenactions, null);
        }

        listener.statements.add(firstStatement);
        listener.statements.add(new ReturnStatementNode(null));
        listener.isStatic = isStatic;
        classDefinitionNode.statements.items.add(listener.getFunctionDefinitionNode());

        /*
            LiveCodeRegistry.getInstance().addEventListener(MethodUpdateEvent.METHOD_UPDATE, this.foo_codeUpdateListener4703382380319456072, false, 0, true);
            or
            LiveCodeRegistry.getInstance().addEventListener(MethodUpdateEvent.METHOD_UPDATE, Main.getColor_codeUpdateListener2123954341648648741, false, 0, true);
         */

        ArgumentListNode args = new ArgumentListNode(TreeUtil.createIdentifier("MethodUpdateEvent", "METHOD_UPDATE"), -1);
        MemberExpressionNode qListenerName = /* isStatic ? TreeUtil.createIdentifier(classDefinitionNode.name.name, listenerName) : */ TreeUtil.createThisIdentifier(listenerName);
        args.items.add(qListenerName);
        args.items.add(new LiteralBooleanNode(false));
        args.items.add(new LiteralNumberNode(String.valueOf(priority)));
        args.items.add(new LiteralBooleanNode(weak));
        CallExpressionNode selector = new CallExpressionNode(new IdentifierNode("addEventListener", -1), args);
        MemberExpressionNode item = new MemberExpressionNode(TreeUtil.createCall("LiveCodeRegistry", "getInstance", null), selector, -1);
        ExpressionStatementNode listenerAddExpressionStatement = new ExpressionStatementNode(new ListNode(null, item, -1));

        if (!isStatic) {
            FunctionDefinitionNode constructorDefinition = TreeUtil.getOrCreateConstructor(classDefinitionNode);
            ObjectList<Node> constructorBody = constructorDefinition.fexpr.body.items;
            constructorBody.add(constructorBody.size() - 1, listenerAddExpressionStatement);
        }

        return listenerAddExpressionStatement;
    }

    /**
     * @return listener add statement
     */
    public static Node addAssetListeners(CompilationUnit unit, ClassDefinitionNode classDefinitionNode, List<VariableDefinitionNode> allEmbedFields, boolean isStatic) {
        List<VariableDefinitionNode> embedFields = allEmbedFields.stream().filter(variableDefinitionNode -> isStatic == TreeNavigator.isStaticField(variableDefinitionNode)).collect(Collectors.toList());
        if (embedFields.isEmpty()) {
            return null;
        }

        TreeUtil.addImport(unit, "codeOrchestra.actionScript.liveCoding.util", "AssetUpdateEvent");

        MethodCONode assetsUpdateListener = new MethodCONode("assetsUpdateListener" + StringUtils.generateId(), null, classDefinitionNode.cx);
        assetsUpdateListener.isStatic = isStatic;
        assetsUpdateListener.addParameter("event", "AssetUpdateEvent");
        FunctionDefinitionNode assetsBroadcastMethod = assetsUpdateListener.getFunctionDefinitionNode();
        assetsBroadcastMethod.pkgdef = classDefinitionNode.pkgdef;

        for (VariableDefinitionNode variableDefinitionNode : embedFields) {
            if (isStatic != TreeNavigator.isStaticField(variableDefinitionNode)) {
                continue;
            }

            MetaDataNode embed = TreeNavigator.getAnnotation(variableDefinitionNode, "Embed");
            if (embed == null) {
                continue;
            }

            String source = embed.getValue("source") != null ? embed.getValue("source") : embed.getValue(0);
            if (StringUtils.isEmpty(source)) {
                continue;
            }
            String mimeType = embed.getValue("mimeType");

            VariableBindingNode var
                    = (VariableBindingNode) variableDefinitionNode.list.items.get(0);

            BinaryExpressionNode eventCondition = new BinaryExpressionNode(
                    Tokens.EQUALS_TOKEN,
                    TreeUtil.createIdentifier("event", "source"),
                    new LiteralStringNode(source)
            );
            ListNode condition = new ListNode(
                    null,
                    mimeType == null ? eventCondition : new BinaryExpressionNode(Tokens.LOGICALAND_TOKEN, eventCondition,
                            new BinaryExpressionNode(
                                    Tokens.EQUALS_TOKEN,
                                    TreeUtil.createIdentifier("event", "mimeType"),
                                    new LiteralStringNode(mimeType)
                            )),
                    -1
            );
            String embedFieldName = var.variable.identifier.name;
            StatementListNode thenActions = new StatementListNode(new ExpressionStatementNode(new ListNode(
                    null,
                    new ExpressionStatementNode(
                            new ListNode(null, new MemberExpressionNode(TreeNavigator.isStaticField(var) ? TreeUtil.createIdentifier(classDefinitionNode.name.name) : new ThisExpressionNode(), new SetExpressionNode(TreeUtil.createIdentifier(embedFieldName), new ArgumentListNode(TreeUtil.createIdentifier("event", "assetClass"), -1)),
                                    -1), -1
                            )), -1)));

            // Call listeners
            List<FunctionDefinitionNode> liveAssetUpdateListeners = TreeNavigator.getMethodDefinitionsWithAnnotation(classDefinitionNode, "LiveAssetUpdateListener");
            for (FunctionDefinitionNode liveAssetUpdateListener : liveAssetUpdateListeners) {
                MetaDataNode liveAssetUpdateListenerAnnotation = TreeNavigator.getAnnotation(liveAssetUpdateListener, "LiveAssetUpdateListener");
                String sourceParam = liveAssetUpdateListenerAnnotation.getValue("source");
                String fieldParam = liveAssetUpdateListenerAnnotation.getValue("field");

                boolean validField = (!StringUtils.isEmpty(embedFieldName)) && (fieldParam != null && fieldParam.equals(embedFieldName));
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
           LiveCodeRegistry.getInstance().addEventListener(AssetUpdateEvent.ASSET_UPDATE, this.assetsUpdateListener7055724444913929522, false, 10, true);
        */
        ArgumentListNode args = new ArgumentListNode(TreeUtil.createIdentifier("AssetUpdateEvent", "ASSET_UPDATE"), -1);
        MemberExpressionNode qListenerName = TreeUtil.createThisIdentifier(assetsUpdateListener.methodName);
        args.items.add(qListenerName);
        args.items.add(new LiteralBooleanNode(false));
        args.items.add(new LiteralNumberNode(String.valueOf("10")));
        args.items.add(new LiteralBooleanNode(true));
        CallExpressionNode selector = new CallExpressionNode(new IdentifierNode("addEventListener", -1), args);
        MemberExpressionNode item = new MemberExpressionNode(TreeUtil.createCall("LiveCodeRegistry", "getInstance", null), selector, -1);
        ExpressionStatementNode listenerAddExpressionStatement = new ExpressionStatementNode(new ListNode(null, item, -1));

        if (!isStatic) {
            FunctionDefinitionNode constructorDefinition = TreeNavigator.getConstructorDefinition(classDefinitionNode);
            if (constructorDefinition == null) {
                MethodCONode constructorRegularNode = new MethodCONode(classDefinitionNode.name.name, null, classDefinitionNode.cx);
                constructorDefinition = constructorRegularNode.getFunctionDefinitionNode();
                constructorDefinition.pkgdef = classDefinitionNode.pkgdef;
                constructorDefinition.fexpr.body.items.add(new ReturnStatementNode(null));
            }
            ObjectList<Node> constructorBody = constructorDefinition.fexpr.body.items;
            constructorBody.add(constructorBody.size() - 1, listenerAddExpressionStatement);
        }

        return listenerAddExpressionStatement;
    }

    public static void processToplevelNamespace(CompilationUnit unit) {
        Object syntaxTree = unit.getSyntaxTree();
        if (syntaxTree != null && syntaxTree instanceof ProgramNode) {
            ProgramNode programNode = (ProgramNode) syntaxTree;
            for (Node item : programNode.statements.items) {
                if (item instanceof PackageDefinitionNode) {
                    PackageDefinitionNode packageDefinitionNode = (PackageDefinitionNode) item;
                    for (Node pkgItem : packageDefinitionNode.statements.items) {
                        if (pkgItem instanceof NamespaceDefinitionNode) {
                            NamespaceDefinitionNode namespaceDefinitionNode = (NamespaceDefinitionNode) pkgItem;
                            if (namespaceDefinitionNode.value != null && namespaceDefinitionNode.value instanceof LiteralStringNode) {
                                DigestManager.getInstance().addNamespace(namespaceDefinitionNode.name.name, ((LiteralStringNode) namespaceDefinitionNode.value).value);
                            }
                        }
                    }
                }
            }
        }
    }

    public static void transformLoopStatement(ClassDefinitionNode parentClass, ClassCONode classCONode, List<Triple<Node, Node, InsertPosition>> deferredInsertions, RegularNode loopStatementRegularNode, LoopStatement loopASTNode) {
        RegularNode parent = loopStatementRegularNode.getParent();

        if (loopASTNode instanceof HasBody) {
            HasBody hasBody = (HasBody) loopASTNode;
            if (hasBody.getBody() == null) {
                hasBody.setBody(new StatementListNode(null));
            }

            if (hasBody.getBody() instanceof StatementListNode) {
                StatementListNode loopBody = (StatementListNode) hasBody.getBody();

                // Var definition
                String id = StringUtils.generateId();
                Node initializer = new BinaryExpressionNode(Tokens.PLUS_TOKEN, new LiteralStringNode(id), TreeUtil.createCall(null, "getTimer", null));
                String varName = "reqId" + id;
                VariableDefinitionNode localVariableStatement = TreeUtil.createLocalVariable(parentClass.pkgdef, varName, "String", initializer);
                if (parent == null) {
                    deferredInsertions.add(new Triple<Node, Node, InsertPosition>((Node) loopASTNode, localVariableStatement, InsertPosition.BEFORE));
                } else if (parent.getASTNode() instanceof StatementListNode) {
                    StatementListNode parentBody = (StatementListNode) parent.getASTNode();
                    parentBody.items.add(parentBody.items.indexOf(loopASTNode), localVariableStatement);
                }

                // LiveCodingCodeFlowUtil.checkLoop call
                loopBody.items.add(0, new ExpressionStatementNode(new ListNode(null,
                        TreeUtil.createCall(
                                "LiveCodingCodeFlowUtil",
                                "checkLoop",
                                new ArgumentListNode(TreeUtil.createIdentifier(varName), -1)
                        ),
                        -1)));

                classCONode.addImport("flash.utils", "getTimer");
            }
        }

        ExpressionStatementNode emptyCheckLoopStatement = new ExpressionStatementNode(new ListNode(null,
                TreeUtil.createCall(
                        "LiveCodingCodeFlowUtil",
                        "checkLoop",
                        new ArgumentListNode(new LiteralStringNode(""), -1)
                ),
                -1));
        if (parent == null) {
            deferredInsertions.add(new Triple<Node, Node, InsertPosition>((Node) loopASTNode, emptyCheckLoopStatement, InsertPosition.AFTER));
        } else if (parent.getASTNode() instanceof StatementListNode) {
            StatementListNode parentBody = (StatementListNode) parent.getASTNode();
            parentBody.items.add(parentBody.items.indexOf(loopASTNode) + 1, emptyCheckLoopStatement);
        }
    }

    public static void transformProtectedAndSuperReferences(String originalClassFqName, MemberExpressionNode memberExpression) {
        Node base = memberExpression.base;
        if (base == null) {
            return;
        }

        if (base instanceof MemberExpressionNode) {
            // thisScope.protectedMember
            MemberExpressionNode memberExpressionBase = (MemberExpressionNode) base;

            if (memberExpressionBase.base == null && memberExpressionBase.selector.getIdentifier().name.equals("thisScope")) {
                // COLT-145
                if (memberExpression.selector.getIdentifier() == null) {
                    return;
                }
                String accessorName = memberExpression.selector.getIdentifier().name;
                IClassDigest visibleOwnerInsideClass = DigestManager.getInstance().findVisibleOwnerOfInstanceMember(originalClassFqName, accessorName);
                if (visibleOwnerInsideClass != null) {
                    IMember instanceMember = visibleOwnerInsideClass.getInstanceMember(accessorName);
                    if (instanceMember.getVisibility() == Visibility.PROTECTED && !instanceMember.isAddedDuringProcessing()) {
                        memberExpression.selector.getIdentifier().name = accessorName + "_protected" + DigestManager.getInstance().getInheritanceLevel(originalClassFqName);
                    }
                }
            }
        } else if (base instanceof SuperExpressionNode) {
            // super.someMember
            String accessorName = memberExpression.selector.getIdentifier().name;
            IClassDigest visibleOwnerInsideClass = DigestManager.getInstance().findVisibleOwnerOfInstanceMember(originalClassFqName, accessorName);
            if (visibleOwnerInsideClass != null) {
                memberExpression.selector.getIdentifier().name = accessorName + "_overriden_super" + DigestManager.getInstance().getInheritanceLevel(originalClassFqName);
                memberExpression.base = TreeUtil.createIdentifier("thisScope");
            }
        }
    }

    public static void transformMemberReferences(String className, boolean staticMethod, String originalPackageName, String originalClassFqName, Set<String> localVariables, MemberExpressionNode memberExpression) {
        if (memberExpression.base == null) {
            SelectorNode selector = memberExpression.selector;

            // COLT-104 - skip transforming the local variable references
            if (selector != null && selector.getIdentifier() != null && localVariables.contains(selector.getIdentifier().name)) {
                return;
            }

            // COLT-38 - Transform trace() to LogUtil.log()
            if (selector instanceof CallExpressionNode) {
                if ("trace".equals(selector.getIdentifier().name)) {
                    CallExpressionNode callExpressionNode = (CallExpressionNode) selector;

                    ArgumentListNode errorTraceArguments = new ArgumentListNode(new LiteralStringNode("trace"), -1);
                    errorTraceArguments.items.add(new LiteralStringNode(""));
                    errorTraceArguments.items.add(new LiteralStringNode(""));
                    errorTraceArguments.items.add(new LiteralStringNode(originalClassFqName));

                    // CFB-15
                    Node logUtilArg = new LiteralStringNode("");
                    if (callExpressionNode.args != null) {
                        Iterator<Node> traceArgsIterator = callExpressionNode.args.items.iterator();
                        while (traceArgsIterator.hasNext()) {
                            Node traceArg = traceArgsIterator.next();
                            logUtilArg = new BinaryExpressionNode(Tokens.PLUS_TOKEN, logUtilArg, traceArg);

                            if (traceArgsIterator.hasNext()) {
                                logUtilArg = new BinaryExpressionNode(Tokens.PLUS_TOKEN, logUtilArg, new LiteralStringNode(", "));
                            }
                        }
                    }

                    errorTraceArguments.items.add(logUtilArg);

                    callExpressionNode.expr = new IdentifierNode("log", -1);
                    callExpressionNode.args = errorTraceArguments;

                    memberExpression.base = TreeUtil.createIdentifier("LogUtil");
                    return;
                }
            }

            IdentifierNode identifier = selector.getIdentifier();
            if (identifier != null) {
                IClassDigest classDigest = DigestManager.getInstance().getClassDigest(originalClassFqName);
                for (IMember member : classDigest.getAllMembers()) {
                    if (!(memberExpression.selector instanceof CallExpressionNode) && identifier.name.equals(member.getName()) && member.isAddedDuringProcessing() && EnumSet.of(MemberKind.GETTER, MemberKind.GETTER, MemberKind.METHOD).contains(member.getKind())) {
                        memberExpression.base = TreeUtil.createIdentifier("LiveCodeRegistry");
                        ArgumentListNode args = new ArgumentListNode(TreeUtil.createIdentifier("thisScope"), -1);
                        args.items.add(TreeUtil.createIdentifier("thisScope", member.getName()));
                        memberExpression.selector = new CallExpressionNode(new IdentifierNode("delegete", -1), args);
                        return;
                    }
                }

                if (!staticMethod && DigestManager.getInstance().isInstanceMemberVisibleInsideClass(originalClassFqName, identifier.name)) {
                    memberExpression.base = TreeUtil.createIdentifier("thisScope");
                } else if (DigestManager.getInstance().findOwnerOfStaticMember(originalClassFqName, identifier.name) != null) {
                    // CAS-336
                    if (staticMethod) {
                        memberExpression.base = TreeUtil.createIdentifier("classParam");
                    } else {
                        String ownerOfStaticMemberFqName = DigestManager.getInstance().findOwnerOfStaticMember(originalClassFqName, identifier.name);
                        String shortName = StringUtils.shortNameFromLongName(ownerOfStaticMemberFqName);
                        memberExpression.base = TreeUtil.createIdentifier(shortName);
                    }
                } else {
                    String possibleClassName = identifier.name;
                    if (className.equals(possibleClassName)) {
                        return;
                    }

                    Set<String> shortNamesFromPackage = DigestManager.getInstance().getShortNamesFromPackage(originalPackageName);
                    if (shortNamesFromPackage != null && shortNamesFromPackage.contains(possibleClassName)) {
                        selector.expr = new QualifiedIdentifierNode(new LiteralStringNode(originalPackageName), possibleClassName, -1);
                    }
                }
            }
        }
    }


}
