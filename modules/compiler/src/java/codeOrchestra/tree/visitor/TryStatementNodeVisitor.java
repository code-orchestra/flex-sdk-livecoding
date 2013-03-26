package codeOrchestra.tree.visitor;

import macromedia.asc.parser.TryStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class TryStatementNodeVisitor extends NodeVisitor<TryStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(TryStatementNode left, TryStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.tryblock);
        stuffToCompare.rightChildren.add(right.tryblock);

        stuffToCompare.leftChildren.add(left.catchlist);
        stuffToCompare.rightChildren.add(right.catchlist);

        stuffToCompare.leftChildren.add(left.finallyblock);
        stuffToCompare.rightChildren.add(right.finallyblock);

        stuffToCompare.leftLeaves.add(left.finallyInserted);
        stuffToCompare.rightLeaves.add(right.finallyInserted);

        stuffToCompare.leftLeaves.add(left.loop_index);
        stuffToCompare.rightLeaves.add(right.loop_index);

        return stuffToCompare;
    }
}
