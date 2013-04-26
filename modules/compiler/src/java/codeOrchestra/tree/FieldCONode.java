package codeOrchestra.tree;

import macromedia.asc.parser.*;
import macromedia.asc.util.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class FieldCONode extends CONode {

    // Set by user
    public String fieldName;
    public String fieldType;
    public Node initializer;
    public boolean isStatic;

    // Set by parent node generator
    PackageDefinitionNode packageDefinitionNode;

    // Set by current node generator
    VariableDefinitionNode variableDefinitionNode;

    public FieldCONode(String fieldName, String fieldType) {
        this.fieldName = fieldName;
        this.fieldType = fieldType;
    }

    @Override
    protected void generateTree() {
        AttributeListNode attrs = new AttributeListNode(TreeUtil.createPublicModifier(), -1);
        if (isStatic) {
            attrs.items.add(TreeUtil.createStaticModifier());
        }
        ListNode list = new ListNode(null, new VariableBindingNode(
                packageDefinitionNode,
                attrs,
                Tokens.VAR_TOKEN,
                new TypedIdentifierNode(
                        new QualifiedIdentifierNode(attrs, fieldName, -1),
                        new TypeExpressionNode(TreeUtil.createIdentifier(fieldType), true, false),
                        -1
                ),
                initializer
        ), -1);
        variableDefinitionNode = new VariableDefinitionNode(packageDefinitionNode, attrs, Tokens.VAR_TOKEN, list, -1);
    }

    public VariableDefinitionNode getVariableDefinitionNode() {
        generateTree();
        return variableDefinitionNode;
    }

}
