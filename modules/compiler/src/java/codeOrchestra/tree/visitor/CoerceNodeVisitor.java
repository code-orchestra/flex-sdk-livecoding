package codeOrchestra.tree.visitor;

import macromedia.asc.parser.CoerceNode;

/**
 * @author Anton.I.Neverov
 */
public class CoerceNodeVisitor extends NodeVisitor<CoerceNode> {
    @Override
    protected StuffToCompare createStuffToCompare(CoerceNode left, CoerceNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.expr);
        stuffToCompare.rightChildren.add(right.expr);

        stuffToCompare.leftLeaves.add(left.actual);
        stuffToCompare.rightLeaves.add(right.actual);

        stuffToCompare.leftLeaves.add(left.expected);
        stuffToCompare.rightLeaves.add(right.expected);

        stuffToCompare.leftLeaves.add(left.void_result);
        stuffToCompare.rightLeaves.add(right.void_result);

        stuffToCompare.leftLeaves.add(left.is_explicit);
        stuffToCompare.rightLeaves.add(right.is_explicit);

        return stuffToCompare;
    }
}
