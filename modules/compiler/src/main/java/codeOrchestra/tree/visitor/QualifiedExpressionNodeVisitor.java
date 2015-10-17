package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.QualifiedExpressionNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class QualifiedExpressionNodeVisitor extends QualifiedIdentifierNodeVisitor<QualifiedExpressionNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final QualifiedExpressionNode node) {
        return new LinkedHashMap<Node, String>() {{
            putAll(QualifiedExpressionNodeVisitor.super.getChildren(node));
            put(node.expr, "expr");
        }};
    }

    @Override
    protected List<Object> getLeaves(final QualifiedExpressionNode node) {
        return new ArrayList<Object>() {{
            addAll(QualifiedExpressionNodeVisitor.super.getLeaves(node));
            addAll(node.nss);
        }};
    }
}
