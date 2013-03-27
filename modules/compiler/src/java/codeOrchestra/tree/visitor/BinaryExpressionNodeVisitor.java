package codeOrchestra.tree.visitor;

import macromedia.asc.parser.BinaryExpressionNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class BinaryExpressionNodeVisitor extends NodeVisitor<BinaryExpressionNode> {
    @Override
    protected List<Node> getChildren(final BinaryExpressionNode node) {
        return new ArrayList<Node>() {{
            add(node.lhs);
            add(node.rhs);
        }};
    }

    @Override
    protected List<Object> getLeaves(final BinaryExpressionNode node) {
        return new ArrayList<Object>() {{
            add(node.op);
            add(node.lhstype);
            add(node.rhstype);
        }};
    }
}
