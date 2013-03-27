package codeOrchestra.tree.visitor;

import macromedia.asc.parser.CallExpressionNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class CallExpressionNodeVisitor extends SelectorNodeVisitor<CallExpressionNode> {
    @Override
    protected List<Node> getChildren(final CallExpressionNode node) {
        return new ArrayList<Node>() {{
            addAll(CallExpressionNodeVisitor.super.getChildren(node));
            add(node.args);
        }};
    }

    @Override
    protected List<Object> getLeaves(final CallExpressionNode node) {
        return new ArrayList<Object>() {{
            addAll(CallExpressionNodeVisitor.super.getLeaves(node));
            add(node.is_new);
        }};
    }
}
