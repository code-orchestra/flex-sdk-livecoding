package codeOrchestra.tree.visitor;

import macromedia.asc.parser.DeleteExpressionNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class DeleteExpressionNodeVisitor extends SelectorNodeVisitor<DeleteExpressionNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(DeleteExpressionNode node) {
        return super.getChildren(node);
    }

    @Override
    protected List<Object> getLeaves(final DeleteExpressionNode node) {
        return new ArrayList<Object>() {{
            addAll(DeleteExpressionNodeVisitor.super.getLeaves(node));
            add(node.op);
        }};
    }
}
