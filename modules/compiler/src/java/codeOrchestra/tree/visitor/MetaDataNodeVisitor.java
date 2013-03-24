package codeOrchestra.tree.visitor;

import macromedia.asc.parser.MetaDataNode;

/**
 * @author Anton.I.Neverov
 */
public class MetaDataNodeVisitor extends NodeVisitor<MetaDataNode> {
    @Override
    protected StuffToCompare createStuffToCompare(MetaDataNode left, MetaDataNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.data);
        stuffToCompare.rightChildren.add(right.data);

        // TODO: Could this be a back-reference?
        stuffToCompare.leftChildren.add(left.def);
        stuffToCompare.rightChildren.add(right.def);

        stuffToCompare.leftLeaves.add(left.getMetadata());
        stuffToCompare.rightLeaves.add(right.getMetadata());

        return stuffToCompare;
    }
}
