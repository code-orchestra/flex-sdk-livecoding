package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.ParenExpressionNode;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ParenExpressionNodeVisitor extends NodeVisitor<ParenExpressionNode> {
    @Override
    protected List<Node> getChildren(ParenExpressionNode node) {
        return Collections.singletonList(node.expr);
    }

    @Override
    protected List<Object> getLeaves(ParenExpressionNode node) {
        return Collections.emptyList();
    }
}
