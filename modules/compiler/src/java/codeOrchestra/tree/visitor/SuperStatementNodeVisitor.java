package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.SuperStatementNode;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class SuperStatementNodeVisitor extends NodeVisitor<SuperStatementNode> {
    @Override
    protected List<Node> getChildren(SuperStatementNode node) {
        return Collections.<Node>singletonList(node.call);
    }

    @Override
    protected List<Object> getLeaves(SuperStatementNode node) {
        return Collections.<Object>singletonList(node.baseobj);
    }
}
