package codeOrchestra.tree.visitor;

import macromedia.asc.parser.SelectorNode;

/**
 * @author Anton.I.Neverov
 */
public abstract class SelectorNodeVisitor<N extends SelectorNode> extends NodeVisitor<N> {
    @Override
    protected StuffToCompare createStuffToCompare(SelectorNode left, SelectorNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.expr);
        stuffToCompare.rightChildren.add(right.expr);

        stuffToCompare.leftLeaves.add(left.base);
        stuffToCompare.rightLeaves.add(right.base);

        stuffToCompare.leftLeaves.add(left.ref);
        stuffToCompare.rightLeaves.add(right.ref);

        stuffToCompare.leftLeaves.add(left.is_package);
        stuffToCompare.rightLeaves.add(right.is_package);

        stuffToCompare.leftLeaves.add(left.getFlags());
        stuffToCompare.rightLeaves.add(right.getFlags());

        return stuffToCompare;
    }
}
