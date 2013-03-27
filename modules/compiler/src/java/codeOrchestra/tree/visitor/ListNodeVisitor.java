package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ListNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ListNodeVisitor<N extends ListNode> extends NodeVisitor<N> {
    @Override
    protected List<Node> getChildren(N node) {
        return node.items;
    }

    @Override
    protected List<Object> getLeaves(N node) {
        return new ArrayList<Object>(node.values);
    }
}
