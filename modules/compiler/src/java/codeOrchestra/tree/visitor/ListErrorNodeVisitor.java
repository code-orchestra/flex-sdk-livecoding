package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ListErrorNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ListErrorNodeVisitor extends ListNodeVisitor<ListErrorNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final ListErrorNode node) {
        return super.getChildren(node);
    }

    @Override
    protected List<Object> getLeaves(final ListErrorNode node) {
        return new ArrayList<Object>() {{
            addAll(ListErrorNodeVisitor.super.getLeaves(node));
            add(node.value);
        }};
    }
}
