package codeOrchestra.tree.visitor;

import macromedia.asc.parser.DoStatementNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class DoStatementNodeVisitor extends NodeVisitor<DoStatementNode> {
    @Override
    protected List<Node> getChildren(final DoStatementNode node) {
        return new ArrayList<Node>() {{
            add(node.statements);
            add(node.expr);
        }};
    }

    @Override
    protected List<Object> getLeaves(DoStatementNode node) {
        return Collections.emptyList();
    }
}
