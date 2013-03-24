package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ConditionalExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class ConditionalExpressionNodeVisitor extends NodeVisitor<ConditionalExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ConditionalExpressionNode left, ConditionalExpressionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.condition);
        stuffToCompare.rightChildren.add(right.condition);

        stuffToCompare.leftChildren.add(left.thenexpr);
        stuffToCompare.rightChildren.add(right.thenexpr);

        stuffToCompare.leftChildren.add(left.elseexpr);
        stuffToCompare.rightChildren.add(right.elseexpr);

        stuffToCompare.leftLeaves.add(left.thenvalue);
        stuffToCompare.rightLeaves.add(right.thenvalue);

        stuffToCompare.leftLeaves.add(left.elsevalue);
        stuffToCompare.rightLeaves.add(right.elsevalue);

        return stuffToCompare;
    }
}
