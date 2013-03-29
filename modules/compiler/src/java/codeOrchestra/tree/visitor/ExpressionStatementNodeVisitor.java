package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ExpressionStatementNode;
import macromedia.asc.parser.Node;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class ExpressionStatementNodeVisitor extends NodeVisitor<ExpressionStatementNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final ExpressionStatementNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.expr, "expr");
        }};
    }

    @Override
    protected List<Object> getLeaves(final ExpressionStatementNode node) {
        return new ArrayList<Object>() {{
            add(node.gen_bits);
            add(node.ref);
            add(node.expected_type);
            add(node.is_var_stmt);
        }};
    }
}
