package codeOrchestra.tree.visitor;

import macromedia.asc.parser.DoStatementNode;
import macromedia.asc.parser.Node;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class DoStatementNodeVisitor extends NodeVisitor<DoStatementNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final DoStatementNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.statements, "statements");
            put(node.expr, "expr");
        }};
    }

    @Override
    protected List<Object> getLeaves(DoStatementNode node) {
        return Collections.emptyList();
    }
}
