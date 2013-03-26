package codeOrchestra.tree.visitor;

import macromedia.asc.parser.UsePrecisionNode;

/**
 * @author Anton.I.Neverov
 */
public class UsePrecisionNodeVisitor extends UsePragmaNodeVisitor<UsePrecisionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(UsePrecisionNode left, UsePrecisionNode right) {
        StuffToCompare stuffToCompare = super.createStuffToCompare(left, right);

        stuffToCompare.leftLeaves.add(left.precision);
        stuffToCompare.rightLeaves.add(right.precision);

        return stuffToCompare;
    }
}
