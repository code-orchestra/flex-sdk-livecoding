package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LabeledStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class LabeledStatementNodeVisitor extends NodeVisitor<LabeledStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(LabeledStatementNode left, LabeledStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.label);
        stuffToCompare.rightChildren.add(right.label);

        stuffToCompare.leftChildren.add(left.statement);
        stuffToCompare.rightChildren.add(right.statement);

        stuffToCompare.leftLeaves.add(left.loop_index);
        stuffToCompare.rightLeaves.add(right.loop_index);

        stuffToCompare.leftLeaves.add(left.is_loop_label);
        stuffToCompare.rightLeaves.add(right.is_loop_label);

        return stuffToCompare;
    }
}
