package codeOrchestra.tree.visitor;

import macromedia.asc.parser.RestExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class RestExpressionNodeVisitor extends NodeVisitor<RestExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(RestExpressionNode left, RestExpressionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.expr);
        stuffToCompare.rightChildren.add(right.expr);

        return stuffToCompare;
    }
}
