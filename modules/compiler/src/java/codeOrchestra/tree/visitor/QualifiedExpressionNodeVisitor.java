package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.QualifiedExpressionNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class QualifiedExpressionNodeVisitor extends QualifiedIdentifierNodeVisitor<QualifiedExpressionNode> {
    @Override
    protected List<Node> getChildren(final QualifiedExpressionNode node) {
        return new ArrayList<Node>() {{
            addAll(QualifiedExpressionNodeVisitor.super.getChildren(node));
            add(node.expr);
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
