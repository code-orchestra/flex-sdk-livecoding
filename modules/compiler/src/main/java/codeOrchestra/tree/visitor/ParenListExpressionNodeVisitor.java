package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.ParenListExpressionNode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ParenListExpressionNodeVisitor extends NodeVisitor<ParenListExpressionNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final ParenListExpressionNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.expr, "expr");
        }};
    }

    @Override
    protected List<Object> getLeaves(ParenListExpressionNode node) {
        return Collections.emptyList();
    }
}
