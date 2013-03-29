package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.ParenExpressionNode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ParenExpressionNodeVisitor extends NodeVisitor<ParenExpressionNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final ParenExpressionNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.expr, "expr");
        }};
    }

    @Override
    protected List<Object> getLeaves(ParenExpressionNode node) {
        return Collections.emptyList();
    }
}
