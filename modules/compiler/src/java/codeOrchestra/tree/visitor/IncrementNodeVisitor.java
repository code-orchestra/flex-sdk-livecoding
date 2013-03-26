package codeOrchestra.tree.visitor;

import macromedia.asc.parser.IncrementNode;

/**
 * @author Anton.I.Neverov
 */
public class IncrementNodeVisitor extends SelectorNodeVisitor<IncrementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(IncrementNode left, IncrementNode right) {
        StuffToCompare stuffToCompare = super.createStuffToCompare(left, right);

        stuffToCompare.leftLeaves.add(left.op);
        stuffToCompare.rightLeaves.add(right.op);

        stuffToCompare.leftLeaves.add(left.isPostfix);
        stuffToCompare.rightLeaves.add(right.isPostfix);

        stuffToCompare.leftLeaves.add(left.numberUsage);
        stuffToCompare.rightLeaves.add(right.numberUsage);

        return stuffToCompare;
    }
}
