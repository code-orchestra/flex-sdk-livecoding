package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ParenExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class ParenExpressionNodeVisitor extends NodeVisitor<ParenExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ParenExpressionNode left, ParenExpressionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.expr);
        stuffToCompare.rightChildren.add(right.expr);

        return stuffToCompare;
    }
}
