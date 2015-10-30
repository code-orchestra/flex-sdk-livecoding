package codeOrchestra.tree.visitor;

import macromedia.asc.parser.GetExpressionNode;
import macromedia.asc.parser.Node;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class GetExpressionNodeVisitor extends SelectorNodeVisitor<GetExpressionNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(GetExpressionNode node) {
        return super.getChildren(node);
    }

    @Override
    protected List<Object> getLeaves(GetExpressionNode node) {
        return super.getLeaves(node);
    }
}
