package codeOrchestra.tree.visitor;

import macromedia.asc.parser.StoreRegisterNode;

/**
 * @author Anton.I.Neverov
 */
public class StoreRegisterNodeVisitor extends NodeVisitor<StoreRegisterNode> {
    @Override
    protected StuffToCompare createStuffToCompare(StoreRegisterNode left, StoreRegisterNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.reg);
        stuffToCompare.rightChildren.add(right.reg);

        stuffToCompare.leftChildren.add(left.expr);
        stuffToCompare.rightChildren.add(right.expr);

        stuffToCompare.leftLeaves.add(left.type);
        stuffToCompare.rightLeaves.add(right.type);

        stuffToCompare.leftLeaves.add(left.void_result);
        stuffToCompare.rightLeaves.add(right.void_result);

        return stuffToCompare;
    }
}
