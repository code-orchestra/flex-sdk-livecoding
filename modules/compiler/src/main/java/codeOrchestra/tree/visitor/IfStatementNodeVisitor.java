package codeOrchestra.tree.visitor;

import macromedia.asc.parser.IfStatementNode;
import macromedia.asc.parser.Node;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class IfStatementNodeVisitor extends NodeVisitor<IfStatementNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final IfStatementNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.condition, "condition");
            put(node.thenactions, "thenactions");
            put(node.elseactions, "elseactions");
        }};
    }

    @Override
    protected List<Object> getLeaves(IfStatementNode node) {
        return Collections.emptyList();
    }
}
