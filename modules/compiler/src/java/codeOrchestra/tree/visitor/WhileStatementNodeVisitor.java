package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.WhileStatementNode;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class WhileStatementNodeVisitor extends NodeVisitor<WhileStatementNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final WhileStatementNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.expr, "expr");
            put(node.statement, "statement");
        }};
    }

    @Override
    protected List<Object> getLeaves(WhileStatementNode node) {
        return Collections.emptyList();
    }
}
