package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.UnaryExpressionNode;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class UnaryExpressionNodeVisitor extends NodeVisitor<UnaryExpressionNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final UnaryExpressionNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.expr, "expr");
        }};
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
