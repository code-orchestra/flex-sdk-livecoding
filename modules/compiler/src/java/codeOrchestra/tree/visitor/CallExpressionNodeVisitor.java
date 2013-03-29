package codeOrchestra.tree.visitor;

import macromedia.asc.parser.CallExpressionNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class CallExpressionNodeVisitor extends SelectorNodeVisitor<CallExpressionNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final CallExpressionNode node) {
        return new LinkedHashMap<Node, String>() {{
            putAll(CallExpressionNodeVisitor.super.getChildren(node));
            put(node.args, "args");
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
