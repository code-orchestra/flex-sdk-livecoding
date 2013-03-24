package codeOrchestra.tree.visitor;

import macromedia.asc.parser.BoxNode;

/**
 * @author Anton.I.Neverov
 */
public class BoxNodeVisitor extends NodeVisitor<BoxNode> {
    @Override
    protected StuffToCompare createStuffToCompare(BoxNode left, BoxNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.expr);
        stuffToCompare.rightChildren.add(right.expr);

        stuffToCompare.leftLeaves.add(left.actual);
        stuffToCompare.rightLeaves.add(right.actual);

        stuffToCompare.leftLeaves.add(left.void_result);
        stuffToCompare.rightLeaves.add(right.void_result);

        return stuffToCompare;
    }
}
