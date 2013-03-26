package codeOrchestra.tree.visitor;

import macromedia.asc.parser.UnaryExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class UnaryExpressionNodeVisitor extends NodeVisitor<UnaryExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(UnaryExpressionNode left, UnaryExpressionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.expr);
        stuffToCompare.rightChildren.add(right.expr);

        stuffToCompare.leftLeaves.add(left.op);
        stuffToCompare.rightLeaves.add(right.op);

        stuffToCompare.leftLeaves.add(left.ref);
        stuffToCompare.rightLeaves.add(right.ref);

        // TODO: Slot?

        stuffToCompare.leftLeaves.add(left.numberUsage);
        stuffToCompare.rightLeaves.add(right.numberUsage);

        return stuffToCompare;
    }
}
