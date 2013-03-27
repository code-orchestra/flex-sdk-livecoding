package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ExpressionStatementNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ExpressionStatementNodeVisitor extends NodeVisitor<ExpressionStatementNode> {
    @Override
    protected List<Node> getChildren(ExpressionStatementNode node) {
        return Collections.singletonList(node.expr);
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
