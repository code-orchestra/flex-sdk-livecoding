package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.ThrowStatementNode;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ThrowStatementNodeVisitor extends NodeVisitor<ThrowStatementNode> {
    @Override
    protected List<Node> getChildren(ThrowStatementNode node) {
        return Collections.singletonList(node.expr);
    }

    @Override
    protected List<Object> getLeaves(ThrowStatementNode node) {
        return Collections.<Object>singletonList(node.finallyInserted);
    }
}
