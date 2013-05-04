package codeOrchestra.tree;

import macromedia.asc.parser.*;
import macromedia.asc.util.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class MethodCONode extends CONode {

    // Set by user
    public String methodName;
    public String returnType;
    public boolean isStatic;
    public final List<Node> statements = new ArrayList<Node>();
    private final List<String[]> parameters = new ArrayList<String[]>();
    private Context cx;

    // Set by parent node generator
    PackageDefinitionNode packageDefinitionNode;

    // Set by current node generator
    FunctionDefinitionNode functionDefinitionNode;
    private StatementListNode functionBody;

    private String namespaceVisibility;

    public MethodCONode(String methodName, String returnType, Context cx) {
        this.methodName = methodName;
        this.returnType = returnType;
        this.cx = cx;
    }

    public void setNamespaceVisibility(String namespaceVisibility) {
        this.namespaceVisibility = namespaceVisibility;
    }

    public void addParameter(String paramName, String paramType) {
        parameters.add(new String[] {paramName, paramType});
    }

    public FunctionDefinitionNode getFunctionDefinitionNode() {
        generateTree();
        return functionDefinitionNode;
    }

    @Override
    protected void generateTree() {
        generateSelf();
        functionBody.items.addAll(statements);
    }

    private void generateSelf() {
        AttributeListNode attrs = new AttributeListNode(namespaceVisibility != null ? TreeUtil.createIdentifier(namespaceVisibility) : TreeUtil.createPublicModifier(), -1);
        if (isStatic) {
            attrs.items.add(TreeUtil.createStaticModifier());
        }
        QualifiedIdentifierNode qualifiedIdentifierNode = new QualifiedIdentifierNode(attrs, methodName, -1);
        functionBody = new StatementListNode(null);
        ParameterListNode parameterListNode = null;
        if (!parameters.isEmpty()) {
            parameterListNode = new ParameterListNode(
                    null,
                    TreeUtil.createParameterNode(parameters.get(0)[0], parameters.get(0)[1]),
                    -1
            );
            parameterListNode.decl_styles.add((byte) 0);
            for (int i = 1; i < parameters.size(); i++) {
                parameterListNode.items.add(TreeUtil.createParameterNode(parameters.get(i)[0], parameters.get(i)[1]));
                parameterListNode.decl_styles.add((byte) 0);
            }
        }
        TypeExpressionNode result = null;
        if (returnType != null) {
            result = new TypeExpressionNode(
                    TreeUtil.createIdentifier(returnType),
                    true,
                    false
            );
        }
        FunctionCommonNode fexpr = new FunctionCommonNode(
                cx,
                null,
                methodName + "$0", // TODO: WTF?!!
                qualifiedIdentifierNode,
                new FunctionSignatureNode(parameterListNode, result),
                functionBody,
                true
        );
        functionDefinitionNode = new FunctionDefinitionNode(
                cx,
                packageDefinitionNode,
                attrs,
                new FunctionNameNode(Tokens.EMPTY_TOKEN, qualifiedIdentifierNode),
                fexpr
        );
    }

}
