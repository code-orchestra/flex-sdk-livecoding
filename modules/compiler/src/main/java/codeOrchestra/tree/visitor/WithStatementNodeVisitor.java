package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.WithStatementNode;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class WithStatementNodeVisitor extends NodeVisitor<WithStatementNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final WithStatementNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.expr, "expr");
            put(node.statement, "statement");
        }};
    }

    @Override
    protected List<Object> getLeaves(WithStatementNode node) {
        return Collections.<Object>singletonList(node.activation);
    }
}
