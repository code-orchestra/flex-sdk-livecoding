package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.RestExpressionNode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class RestExpressionNodeVisitor extends NodeVisitor<RestExpressionNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final RestExpressionNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.expr, "expr");
        }};
    }

    @Override
    protected List<Object> getLeaves(RestExpressionNode node) {
        return Collections.emptyList();
    }
}
