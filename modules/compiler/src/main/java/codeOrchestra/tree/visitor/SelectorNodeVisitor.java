package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.SelectorNode;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public abstract class SelectorNodeVisitor<N extends SelectorNode> extends NodeVisitor<N> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final N node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.expr, "expr");
        }};
    }

    @Override
    protected List<Object> getLeaves(final N node) {
        return new ArrayList<Object>() {{
            add(node.base);
            add(node.ref);
            add(node.is_package);
            add(node.getFlags());
        }};
    }
}
