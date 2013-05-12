package codeOrchestra;

import codeOrchestra.digest.DigestManager;
import codeOrchestra.digest.IClassDigest;
import codeOrchestra.digest.IMember;
import codeOrchestra.digest.Visibility;
import codeOrchestra.tree.ClassCONode;
import codeOrchestra.tree.RegularNode;
import codeOrchestra.tree.TreeUtil;
import codeOrchestra.util.Pair;
import codeOrchestra.util.StringUtils;
import flex2.compiler.CompilationUnit;
import macromedia.asc.parser.*;

import java.util.List;
import java.util.Set;

/**
 * @author Alexander Eliseyev
 */
public class Transformations {

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
