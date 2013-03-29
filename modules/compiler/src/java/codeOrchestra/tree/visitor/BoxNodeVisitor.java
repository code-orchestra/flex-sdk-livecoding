package codeOrchestra.tree.visitor;

import macromedia.asc.parser.BoxNode;
import macromedia.asc.parser.Node;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class BoxNodeVisitor extends NodeVisitor<BoxNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final BoxNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.expr, "expr");
        }};
    }

    @Override
    protected List<Object> getLeaves(final BoxNode node) {
        return new ArrayList<Object>() {{
            add(node.actual);
            add(node.void_result);
        }};
    }
}
