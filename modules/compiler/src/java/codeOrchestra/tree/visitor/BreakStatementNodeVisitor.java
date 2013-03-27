package codeOrchestra.tree.visitor;

import macromedia.asc.parser.BreakStatementNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class BreakStatementNodeVisitor extends NodeVisitor<BreakStatementNode> {
    @Override
    protected List<Node> getChildren(BreakStatementNode node) {
        return Collections.<Node>singletonList(node.id);
    }

    @Override
    protected List<Object> getLeaves(BreakStatementNode node) {
        return Collections.<Object>singletonList(node.loop_index);
    }
}
