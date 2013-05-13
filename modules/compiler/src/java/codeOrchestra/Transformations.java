package codeOrchestra;

import codeOrchestra.digest.*;
import codeOrchestra.tree.*;
import codeOrchestra.util.Pair;
import codeOrchestra.util.StringUtils;
import flex2.compiler.CompilationUnit;
import macromedia.asc.parser.*;
import macromedia.asc.util.ObjectList;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

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

    public static void addAssetListeners(CompilationUnit unit, ClassDefinitionNode classDefinitionNode, String packageName, List<VariableDefinitionNode> embedFields) {
        TreeUtil.addImport(unit, "codeOrchestra.actionScript.liveCoding.util", "AssetUpdateEvent");

        MethodCONode assetsUpdateListener = new MethodCONode("assetsUpdateListener" + StringUtils.generateId(), null, classDefinitionNode.cx);
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

            String sourcePrefixedByPackage;
            if (!StringUtils.isEmpty(packageName)) {
                // Handle ".." in source
                if (source.contains("../")) {
                    try {
                        StringBuilder pathBuilder = new StringBuilder();
                        int goUpCount = org.apache.commons.lang3.StringUtils.countMatches(source, "../");
                        String[] packageSplit = packageName.split("\\.");
                        for (int i = 0; i < packageSplit.length - goUpCount; i++) {
                            pathBuilder.append(packageSplit[i]).append("/");
                        }
                        pathBuilder.append(source.replace("../", ""));

                        sourcePrefixedByPackage = pathBuilder.toString();
                    } catch (Throwable t) {
                        sourcePrefixedByPackage = packageName.replace(".", "/") + "/" + source;
                    }
                } else {
                    sourcePrefixedByPackage = packageName.replace(".", "/") + "/" + source;
                }
            } else {
                sourcePrefixedByPackage = source;
            }

            ListNode condition = new ListNode(
                    null,
                    new BinaryExpressionNode(
                            Tokens.EQUALS_TOKEN,
                            TreeUtil.createIdentifier("event", "source"),
                            new LiteralStringNode(sourcePrefixedByPackage)
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

    public static void transformLoopStatement(ClassDefinitionNode parentClass, ClassCONode classCONode, List<Pair<Node, Node>> deferredInsertions, RegularNode loopStatementRegularNode, LoopStatement loopASTNode) {
        if (loopASTNode instanceof HasBody && ((HasBody) loopASTNode).getBody() instanceof StatementListNode) {
            StatementListNode loopBody = (StatementListNode) ((HasBody) loopASTNode).getBody();

            // Var definition
            String id = StringUtils.generateId();
            Node initializer = new BinaryExpressionNode(Tokens.PLUS_TOKEN, new LiteralStringNode(id), TreeUtil.createCall(null, "getTimer", null));
            String varName = "reqId" + id;
            loopBody.items.add(0, TreeUtil.createLocalVariable(parentClass.pkgdef, varName, "String", initializer));

            // LiveCodingCodeFlowUtil.checkLoop call
            loopBody.items.add(1, new ExpressionStatementNode(new ListNode(null,
                    TreeUtil.createCall(
                            "LiveCodingCodeFlowUtil",
                            "checkLoop",
                            new ArgumentListNode(TreeUtil.createIdentifier(varName), -1)
                    ),
                    -1)));

            classCONode.addImport("flash.utils", "getTimer");
        }

        ExpressionStatementNode emptyCheckLoopStatement = new ExpressionStatementNode(new ListNode(null,
                TreeUtil.createCall(
                        "LiveCodingCodeFlowUtil",
                        "checkLoop",
                        new ArgumentListNode(new LiteralStringNode(""), -1)
                ),
                -1));

        RegularNode parent = loopStatementRegularNode.getParent();
        if (parent == null) {
            deferredInsertions.add(new Pair<Node, Node>((Node) loopASTNode, emptyCheckLoopStatement));
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
                        String newAccessorName = accessorName + "_protected" + DigestManager.getInstance().getInheritanceLevel(visibleOwnerInsideClass.getFqName());
                        memberExpression.selector.getIdentifier().name = newAccessorName;
                    }
                }
            }
        } else if (base instanceof SuperExpressionNode) {
            // super.someMember
            String accessorName = memberExpression.selector.getIdentifier().name;
            IClassDigest visibleOwnerInsideClass = DigestManager.getInstance().findVisibleOwnerOfInstanceMember(originalClassFqName, accessorName);
            if (visibleOwnerInsideClass != null) {
                String newAccessorName = accessorName + "_overriden_super" + DigestManager.getInstance().getInheritanceLevel(visibleOwnerInsideClass.getFqName());
                memberExpression.selector.getIdentifier().name = newAccessorName;
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
                    errorTraceArguments.items.add(new BinaryExpressionNode(Tokens.PLUS_TOKEN, new LiteralStringNode(""), callExpressionNode.args.items.get(0)));

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
                    String ownerOfStaticMemberFqName = DigestManager.getInstance().findOwnerOfStaticMember(originalClassFqName, identifier.name);
                    String shortName = StringUtils.shortNameFromLongName(ownerOfStaticMemberFqName);
                    memberExpression.base = TreeUtil.createIdentifier(shortName);
                    // TODO: add import!
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
