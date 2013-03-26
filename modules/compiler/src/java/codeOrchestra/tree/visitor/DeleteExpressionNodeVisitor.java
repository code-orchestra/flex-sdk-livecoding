package codeOrchestra.tree.visitor;

import macromedia.asc.parser.DeleteExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class DeleteExpressionNodeVisitor extends SelectorNodeVisitor<DeleteExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(DeleteExpressionNode left, DeleteExpressionNode right) {
        StuffToCompare stuffToCompare = super.createStuffToCompare(left, right);

        // TODO: Slot?

        stuffToCompare.leftLeaves.add(left.op);
        stuffToCompare.rightLeaves.add(right.op);

        return stuffToCompare;
    }
}
