package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ConditionalExpressionNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ConditionalExpressionNodeVisitor extends NodeVisitor<ConditionalExpressionNode> {
    @Override
    protected List<Node> getChildren(final ConditionalExpressionNode node) {
        return new ArrayList<Node>() {{
            add(node.condition);
            add(node.thenexpr);
            add(node.elseexpr);
        }};
    }

    @Override
    protected List<Object> getLeaves(final ConditionalExpressionNode node) {
        return new ArrayList<Object>() {{
            add(node.thenvalue);
            add(node.elsevalue);
        }};
    }
}
