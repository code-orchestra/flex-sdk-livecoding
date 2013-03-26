package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ReturnStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class ReturnStatementNodeVisitor extends NodeVisitor<ReturnStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ReturnStatementNode left, ReturnStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.expr);
        stuffToCompare.rightChildren.add(right.expr);

        stuffToCompare.leftLeaves.add(left.finallyInserted);
        stuffToCompare.rightLeaves.add(right.finallyInserted);

        return stuffToCompare;
    }
}
