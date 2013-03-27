package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.ThisExpressionNode;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ThisExpressionNodeVisitor extends NodeVisitor<ThisExpressionNode> {
    @Override
    protected List<Node> getChildren(ThisExpressionNode node) {
        return Collections.emptyList();
    }

    @Override
    protected List<Object> getLeaves(ThisExpressionNode node) {
        return Collections.emptyList();
    }
}
