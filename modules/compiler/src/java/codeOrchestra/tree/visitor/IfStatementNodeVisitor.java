package codeOrchestra.tree.visitor;

import macromedia.asc.parser.IfStatementNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class IfStatementNodeVisitor extends NodeVisitor<IfStatementNode> {
    @Override
    protected List<Node> getChildren(final IfStatementNode node) {
        return new ArrayList<Node>() {{
            add(node.condition);
            add(node.thenactions);
            add(node.elseactions);
        }};
    }

    @Override
    protected List<Object> getLeaves(IfStatementNode node) {
        return Collections.emptyList();
    }
}
