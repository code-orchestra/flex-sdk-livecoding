package codeOrchestra.tree.visitor;

import macromedia.asc.parser.IfStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class IfStatementNodeVisitor extends NodeVisitor<IfStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(IfStatementNode left, IfStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.condition);
        stuffToCompare.rightChildren.add(right.condition);

        stuffToCompare.leftChildren.add(left.thenactions);
        stuffToCompare.rightChildren.add(right.thenactions);

        stuffToCompare.leftChildren.add(left.elseactions);
        stuffToCompare.rightChildren.add(right.elseactions);

        return stuffToCompare;
    }
}
