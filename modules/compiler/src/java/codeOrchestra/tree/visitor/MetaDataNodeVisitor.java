package codeOrchestra.tree.visitor;

import macromedia.asc.parser.MetaDataNode;

/**
 * @author Anton.I.Neverov
 */
public class MetaDataNodeVisitor<N extends MetaDataNode> extends NodeVisitor<N> {
    @Override
    protected StuffToCompare createStuffToCompare(MetaDataNode left, MetaDataNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.data);
        stuffToCompare.rightChildren.add(right.data);

        // def is a back-reference, ignore it

        stuffToCompare.leftLeaves.add(left.getMetadata());
        stuffToCompare.rightLeaves.add(right.getMetadata());

        return stuffToCompare;
    }
}
