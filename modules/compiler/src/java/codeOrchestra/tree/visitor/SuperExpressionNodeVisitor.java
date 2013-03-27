package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.SuperExpressionNode;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class SuperExpressionNodeVisitor extends NodeVisitor<SuperExpressionNode> {
    @Override
    protected List<Node> getChildren(SuperExpressionNode node) {
        return Collections.singletonList(node.expr);
    }

    @Override
    protected List<Object> getLeaves(SuperExpressionNode node) {
        return Collections.emptyList();
    }
}
