package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.WhileStatementNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class WhileStatementNodeVisitor extends NodeVisitor<WhileStatementNode> {
    @Override
    protected List<Node> getChildren(final WhileStatementNode node) {
        return new ArrayList<Node>() {{
            add(node.expr);
            add(node.statement);
        }};
    }

    @Override
    protected List<Object> getLeaves(WhileStatementNode node) {
        return Collections.emptyList();
    }
}
