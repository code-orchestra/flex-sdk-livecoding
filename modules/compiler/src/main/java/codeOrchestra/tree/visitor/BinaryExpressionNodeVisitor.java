package codeOrchestra.tree.visitor;

import macromedia.asc.parser.BinaryExpressionNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class BinaryExpressionNodeVisitor extends NodeVisitor<BinaryExpressionNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final BinaryExpressionNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.lhs, "lhs");
            put(node.rhs, "rhs");
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
