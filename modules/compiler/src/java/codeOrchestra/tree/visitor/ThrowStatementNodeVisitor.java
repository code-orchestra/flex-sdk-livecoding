package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ThrowStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class ThrowStatementNodeVisitor extends NodeVisitor<ThrowStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ThrowStatementNode left, ThrowStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.expr);
        stuffToCompare.rightChildren.add(right.expr);

        stuffToCompare.leftLeaves.add(left.finallyInserted);
        stuffToCompare.rightLeaves.add(right.finallyInserted);

        return stuffToCompare;
    }
}
