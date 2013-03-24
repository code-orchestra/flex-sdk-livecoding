package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ContinueStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class ContinueStatementNodeVisitor extends NodeVisitor<ContinueStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ContinueStatementNode left, ContinueStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.id);
        stuffToCompare.rightChildren.add(right.id);

        stuffToCompare.leftLeaves.add(left.loop_index);
        stuffToCompare.rightLeaves.add(right.loop_index);

        return stuffToCompare;
    }
}
