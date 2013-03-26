package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ParameterNode;

/**
 * @author Anton.I.Neverov
 */
public class ParameterNodeVisitor<N extends ParameterNode> extends NodeVisitor<N> {
    @Override
    protected StuffToCompare createStuffToCompare(ParameterNode left, ParameterNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.identifier);
        stuffToCompare.rightChildren.add(right.identifier);

        stuffToCompare.leftChildren.add(left.type);
        stuffToCompare.rightChildren.add(right.type);

        stuffToCompare.leftChildren.add(left.init);
        stuffToCompare.rightChildren.add(right.init);

        stuffToCompare.leftChildren.add(left.attrs);
        stuffToCompare.rightChildren.add(right.attrs);

        stuffToCompare.leftLeaves.add(left.kind);
        stuffToCompare.rightLeaves.add(right.kind);

        stuffToCompare.leftLeaves.add(left.ref);
        stuffToCompare.rightLeaves.add(right.ref);

        stuffToCompare.leftLeaves.add(left.typeref);
        stuffToCompare.rightLeaves.add(right.typeref);

        stuffToCompare.leftLeaves.add(left.no_anno);
        stuffToCompare.rightLeaves.add(right.no_anno);

        return stuffToCompare;
    }
}
