package codeOrchestra.tree.visitor;

import macromedia.asc.parser.IncrementNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class IncrementNodeVisitor extends SelectorNodeVisitor<IncrementNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(IncrementNode node) {
        return super.getChildren(node);
    }

    @Override
    protected List<Object> getLeaves(final IncrementNode node) {
        return new ArrayList<Object>() {{
            addAll(IncrementNodeVisitor.super.getLeaves(node));
            add(node.op);
            add(node.isPostfix);
            add(node.numberUsage);
        }};
    }
}
