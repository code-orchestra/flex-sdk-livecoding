package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ForStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class ForStatementNodeVisitor extends NodeVisitor<ForStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ForStatementNode left, ForStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.initialize);
        stuffToCompare.rightChildren.add(right.initialize);

        stuffToCompare.leftChildren.add(left.test);
        stuffToCompare.rightChildren.add(right.test);

        stuffToCompare.leftChildren.add(left.increment);
        stuffToCompare.rightChildren.add(right.increment);

        stuffToCompare.leftChildren.add(left.statement);
        stuffToCompare.rightChildren.add(right.statement);

        stuffToCompare.leftLeaves.add(left.is_forin);
        stuffToCompare.rightLeaves.add(right.is_forin);

        stuffToCompare.leftLeaves.add(left.loop_index);
        stuffToCompare.rightLeaves.add(right.loop_index);

        return stuffToCompare;
    }
}
