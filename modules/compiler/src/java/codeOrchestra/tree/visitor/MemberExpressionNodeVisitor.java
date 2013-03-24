package codeOrchestra.tree.visitor;

import macromedia.asc.parser.MemberExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class MemberExpressionNodeVisitor extends NodeVisitor<MemberExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(MemberExpressionNode left, MemberExpressionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.base);
        stuffToCompare.rightChildren.add(right.base);

        stuffToCompare.leftChildren.add(left.selector);
        stuffToCompare.rightChildren.add(right.selector);

        stuffToCompare.leftLeaves.add(left.ref);
        stuffToCompare.rightLeaves.add(right.ref);

        return stuffToCompare;
    }
}
