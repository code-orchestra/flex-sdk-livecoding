package codeOrchestra.tree.visitor;

import macromedia.asc.parser.WithStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class WithStatementNodeVisitor extends NodeVisitor<WithStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(WithStatementNode left, WithStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.expr);
        stuffToCompare.rightChildren.add(right.expr);

        stuffToCompare.leftChildren.add(left.statement);
        stuffToCompare.rightChildren.add(right.statement);

        stuffToCompare.leftLeaves.add(left.activation);
        stuffToCompare.rightLeaves.add(right.activation);

        return stuffToCompare;
    }
}
