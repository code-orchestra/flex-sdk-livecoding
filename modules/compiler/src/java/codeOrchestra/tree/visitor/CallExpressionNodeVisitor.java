package codeOrchestra.tree.visitor;

import macromedia.asc.parser.CallExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class CallExpressionNodeVisitor extends SelectorNodeVisitor<CallExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(CallExpressionNode left, CallExpressionNode right) {
        StuffToCompare stuffToCompare = super.createStuffToCompare(left, right);

        stuffToCompare.leftChildren.add(left.args);
        stuffToCompare.rightChildren.add(right.args);

        stuffToCompare.leftLeaves.add(left.is_new);
        stuffToCompare.rightLeaves.add(right.is_new);

        return stuffToCompare;
    }
}
