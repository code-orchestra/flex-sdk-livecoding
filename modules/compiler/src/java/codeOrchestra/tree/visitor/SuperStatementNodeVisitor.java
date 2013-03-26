package codeOrchestra.tree.visitor;

import macromedia.asc.parser.SuperStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class SuperStatementNodeVisitor extends NodeVisitor<SuperStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(SuperStatementNode left, SuperStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.call);
        stuffToCompare.rightChildren.add(right.call);

        stuffToCompare.leftLeaves.add(left.baseobj);
        stuffToCompare.rightLeaves.add(right.baseobj);

        return stuffToCompare;
    }
}
