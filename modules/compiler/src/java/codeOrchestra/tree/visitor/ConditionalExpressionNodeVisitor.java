package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ConditionalExpressionNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ConditionalExpressionNodeVisitor extends NodeVisitor<ConditionalExpressionNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final ConditionalExpressionNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.condition, "condition");
            put(node.thenexpr, "thenexpr");
            put(node.elseexpr, "elseexpr");
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
