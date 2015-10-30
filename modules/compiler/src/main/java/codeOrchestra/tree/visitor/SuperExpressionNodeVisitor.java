package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.SuperExpressionNode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class SuperExpressionNodeVisitor extends NodeVisitor<SuperExpressionNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final SuperExpressionNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.expr, "expr");
        }};
    }

    @Override
    protected List<Object> getLeaves(SuperExpressionNode node) {
        return Collections.emptyList();
    }
}
