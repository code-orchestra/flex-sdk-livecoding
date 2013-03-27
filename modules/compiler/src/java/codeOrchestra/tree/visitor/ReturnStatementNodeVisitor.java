package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.ReturnStatementNode;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ReturnStatementNodeVisitor extends NodeVisitor<ReturnStatementNode> {
    @Override
    protected List<Node> getChildren(ReturnStatementNode node) {
        return Collections.singletonList(node.expr);
    }

    @Override
    protected List<Object> getLeaves(ReturnStatementNode node) {
        return Collections.<Object>singletonList(node.finallyInserted);
    }
}
