package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.SetExpressionNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class SetExpressionNodeVisitor extends SelectorNodeVisitor<SetExpressionNode> {
    @Override
    protected List<Node> getChildren(final SetExpressionNode node) {
        return new ArrayList<Node>() {{
            addAll(SetExpressionNodeVisitor.super.getChildren(node));
            add(node.args);
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
