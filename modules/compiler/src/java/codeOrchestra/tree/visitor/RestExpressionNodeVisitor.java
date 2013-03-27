package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.RestExpressionNode;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class RestExpressionNodeVisitor extends NodeVisitor<RestExpressionNode> {
    @Override
    protected List<Node> getChildren(RestExpressionNode node) {
        return Collections.singletonList(node.expr);
    }

    @Override
    protected List<Object> getLeaves(RestExpressionNode node) {
        return Collections.emptyList();
    }
}
