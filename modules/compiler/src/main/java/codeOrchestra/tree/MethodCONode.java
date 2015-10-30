package codeOrchestra.tree;

import macromedia.asc.parser.*;
import macromedia.asc.util.Context;
import org.apache.commons.lang3.SerializationUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 * @author Alexander Eliseyev
 */
public class MethodCONode extends CONode {

    // Set by user
    public String methodName;
    public String returnType;
    public boolean isStatic;
    public final List<Node> statements = new ArrayList<>();
    private final List<Parameter> parameters = new ArrayList<>();
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
        addParameter(paramName, paramType != null ? new TypeExpressionNode(
                TreeUtil.createIdentifier(paramType),
                true,
                false
        ) : null);
    }

    public void addParameter(String paramName, TypeExpressionNode paramType) {
        addParameter(paramName, paramType, null);
    }

    public void addParameter(String paramName, TypeExpressionNode paramType, Node initializer) {
        Parameter parameter = new Parameter(paramName, paramType, initializer);
        if (!parameters.contains(parameter)) {
            parameters.add(parameter);
        }
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
            Parameter firstParameter = parameters.get(0);
            parameterListNode = new ParameterListNode(
                    null,
                    firstParameter.isVector ? TreeUtil.createParameterNode(firstParameter.paramName, (String) null, firstParameter.initializer) : TreeUtil.createParameterNode(firstParameter.paramName, firstParameter.paramTypeString, firstParameter.initializer),
                    -1
            );
            parameterListNode.decl_styles.add((byte) 0);
            for (int i = 1; i < parameters.size(); i++) {
                Parameter parameter = parameters.get(i);
                parameterListNode.items.add(parameter.isVector ? TreeUtil.createParameterNode(parameter.paramName, (String) null, parameter.initializer) : TreeUtil.createParameterNode(parameter.paramName, parameter.paramTypeString, parameter.initializer));
                parameterListNode.decl_styles.add((byte) 0);
            }
        }
        TypeExpressionNode result = null;
        if (returnType != null) {
            if ("Vector".equals(returnType)) {
                result = null;
            } else {
                result = new TypeExpressionNode(
                        TreeUtil.createIdentifier(returnType),
                        true,
                        false
                );
            }
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

    private static class Parameter {
        public String paramName;
        public String paramTypeString;

        public Node initializer;
        public TypeExpressionNode paramTypeNode;

        public boolean isVector;

        private Parameter(String paramName, TypeExpressionNode paramTypeNode, Node initializer) {
            this.paramName = paramName;

            if (paramTypeNode != null) {
                this.paramTypeString = ((IdentifierNode) ((MemberExpressionNode) (paramTypeNode.expr)).selector.expr).name;
                if ("Vector".equals(paramTypeString)) {
                    isVector = true;
                }
            }

            this.paramTypeNode = SerializationUtils.clone(paramTypeNode);
            this.initializer = initializer;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Parameter parameter = (Parameter) o;

            return !(paramName != null ? !paramName.equals(parameter.paramName) : parameter.paramName != null) && !(paramTypeString != null ? !paramTypeString.equals(parameter.paramTypeString) : parameter.paramTypeString != null);

        }

        @Override
        public int hashCode() {
            int result = paramName != null ? paramName.hashCode() : 0;
            result = 31 * result + (paramTypeString != null ? paramTypeString.hashCode() : 0);
            return result;
        }
    }

}
