package codeOrchestra.tree.visitor;

import macromedia.asc.parser.BinaryExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class BinaryExpressionNodeVisitor extends NodeVisitor<BinaryExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(BinaryExpressionNode left, BinaryExpressionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.lhs);
        stuffToCompare.rightChildren.add(right.lhs);

        stuffToCompare.leftChildren.add(left.rhs);
        stuffToCompare.rightChildren.add(right.rhs);

        stuffToCompare.leftLeaves.add(left.op);
        stuffToCompare.rightLeaves.add(right.op);

        // TODO: Do we need to compare slots?

        stuffToCompare.leftLeaves.add(left.lhstype);
        stuffToCompare.rightLeaves.add(right.rhstype);

        stuffToCompare.leftLeaves.add(left.numberUsage);
        stuffToCompare.rightLeaves.add(right.numberUsage);

        return stuffToCompare;
    }
}
