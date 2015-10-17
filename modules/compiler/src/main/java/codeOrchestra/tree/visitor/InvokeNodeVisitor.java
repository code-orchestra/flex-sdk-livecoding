package codeOrchestra.tree.visitor;

import macromedia.asc.parser.InvokeNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class InvokeNodeVisitor extends SelectorNodeVisitor<InvokeNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final InvokeNode node) {
        return new LinkedHashMap<Node, String>() {{
            putAll(InvokeNodeVisitor.super.getChildren(node));
            put(node.args, "args");
        }};
    }

    @Override
    protected List<Object> getLeaves(final InvokeNode node) {
        return new ArrayList<Object>() {{
            addAll(InvokeNodeVisitor.super.getLeaves(node));
            add(node.name);
            add(node.index);
        }};
    }
}
