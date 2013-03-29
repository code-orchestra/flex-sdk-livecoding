package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ListNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ListNodeVisitor<N extends ListNode> extends NodeVisitor<N> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final N node) {
        return new LinkedHashMap<Node, String>() {{
            for (Node item : node.items) {
                put(item, "items");
            }
        }};
    }

    @Override
    protected List<Object> getLeaves(N node) {
        return new ArrayList<Object>(node.values);
    }
}
