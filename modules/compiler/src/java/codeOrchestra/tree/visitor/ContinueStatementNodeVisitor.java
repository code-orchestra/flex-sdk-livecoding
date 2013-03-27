package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ContinueStatementNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ContinueStatementNodeVisitor extends NodeVisitor<ContinueStatementNode> {
    @Override
    protected List<Node> getChildren(ContinueStatementNode node) {
        return Collections.<Node>singletonList(node.id);
    }

    @Override
    protected List<Object> getLeaves(ContinueStatementNode node) {
        return Collections.<Object>singletonList(node.loop_index);
    }
}
