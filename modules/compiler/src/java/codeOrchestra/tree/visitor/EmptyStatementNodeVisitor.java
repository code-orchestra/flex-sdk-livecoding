package codeOrchestra.tree.visitor;

import macromedia.asc.parser.EmptyStatementNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class EmptyStatementNodeVisitor extends NodeVisitor<EmptyStatementNode> {
    @Override
    protected List<Node> getChildren(EmptyStatementNode node) {
        return Collections.emptyList();
    }

    @Override
    protected List<Object> getLeaves(EmptyStatementNode node) {
        return Collections.emptyList();
    }
}
