package codeOrchestra.tree.visitor;

import macromedia.asc.parser.BreakStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class BreakStatementNodeVisitor extends NodeVisitor<BreakStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(BreakStatementNode left, BreakStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.id);
        stuffToCompare.rightChildren.add(right.id);

        stuffToCompare.leftLeaves.add(left.loop_index);
        stuffToCompare.rightLeaves.add(right.loop_index);

        return stuffToCompare;
    }
}
