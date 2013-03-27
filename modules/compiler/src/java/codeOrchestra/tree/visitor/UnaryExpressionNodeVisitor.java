package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.UnaryExpressionNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class UnaryExpressionNodeVisitor extends NodeVisitor<UnaryExpressionNode> {
    @Override
    protected List<Node> getChildren(UnaryExpressionNode node) {
        return Collections.singletonList(node.expr);
    }

    @Override
    protected List<Object> getLeaves(final UnaryExpressionNode node) {
        return new ArrayList<Object>() {{
            add(node.op);
            add(node.ref);
            add(node.numberUsage);
        }};
    }
}
