package codeOrchestra.tree.visitor;

import macromedia.asc.parser.InvokeNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class InvokeNodeVisitor extends SelectorNodeVisitor<InvokeNode> {
    @Override
    protected List<Node> getChildren(final InvokeNode node) {
        return new ArrayList<Node>() {{
            addAll(InvokeNodeVisitor.super.getChildren(node));
            add(node.args);
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
