package codeOrchestra.tree.visitor;

import macromedia.asc.parser.SwitchStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class SwitchStatementNodeVisitor extends NodeVisitor<SwitchStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(SwitchStatementNode left, SwitchStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.expr);
        stuffToCompare.rightChildren.add(right.expr);

        stuffToCompare.leftChildren.add(left.statements);
        stuffToCompare.rightChildren.add(right.statements);

        stuffToCompare.leftLeaves.add(left.loop_index);
        stuffToCompare.rightLeaves.add(right.loop_index);

        return stuffToCompare;
    }
}
