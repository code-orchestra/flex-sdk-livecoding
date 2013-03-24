package codeOrchestra.tree.visitor;

import macromedia.asc.parser.DoStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class DoStatementNodeVisitor extends NodeVisitor<DoStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(DoStatementNode left, DoStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.statements);
        stuffToCompare.rightChildren.add(right.statements);

        stuffToCompare.leftChildren.add(left.expr);
        stuffToCompare.rightChildren.add(right.expr);

        return stuffToCompare;
    }
}
