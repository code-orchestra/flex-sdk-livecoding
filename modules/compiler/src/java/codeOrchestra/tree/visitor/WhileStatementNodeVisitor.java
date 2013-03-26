package codeOrchestra.tree.visitor;

import macromedia.asc.parser.WhileStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class WhileStatementNodeVisitor extends NodeVisitor<WhileStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(WhileStatementNode left, WhileStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.expr);
        stuffToCompare.rightChildren.add(right.expr);


        stuffToCompare.leftChildren.add(left.statement);
        stuffToCompare.rightChildren.add(right.statement);

        return stuffToCompare;
    }
}
