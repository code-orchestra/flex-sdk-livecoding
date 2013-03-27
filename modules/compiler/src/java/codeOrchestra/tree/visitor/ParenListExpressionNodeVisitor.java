package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.ParenListExpressionNode;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ParenListExpressionNodeVisitor extends NodeVisitor<ParenListExpressionNode> {
    @Override
    protected List<Node> getChildren(ParenListExpressionNode node) {
        return Collections.singletonList(node.expr);
    }

    @Override
    protected List<Object> getLeaves(ParenListExpressionNode node) {
        return Collections.emptyList();
    }
}
