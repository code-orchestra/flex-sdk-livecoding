package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.SetExpressionNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class SetExpressionNodeVisitor extends SelectorNodeVisitor<SetExpressionNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final SetExpressionNode node) {
        return new LinkedHashMap<Node, String>() {{
            putAll(SetExpressionNodeVisitor.super.getChildren(node));
            put(node.args, "args");
        }};
    }

    @Override
    protected List<Object> getLeaves(final SetExpressionNode node) {
        return new ArrayList<Object>() {{
            addAll(SetExpressionNodeVisitor.super.getLeaves(node));
            add(node.value_type);
            add(node.is_constinit);
            add(node.is_initializer);
        }};
    }
}
