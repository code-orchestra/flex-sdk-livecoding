package codeOrchestra.tree.visitor;

import macromedia.asc.parser.CoerceNode;
import macromedia.asc.parser.Node;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class CoerceNodeVisitor extends NodeVisitor<CoerceNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final CoerceNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.expr, "expr");
        }};
    }

    @Override
    protected List<Object> getLeaves(final CoerceNode node) {
        return new ArrayList<Object>() {{
            add(node.actual);
            add(node.expected);
            add(node.void_result);
            add(node.is_explicit);
        }};
    }
}
