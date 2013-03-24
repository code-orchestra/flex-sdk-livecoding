package codeOrchestra.tree.visitor;

import macromedia.asc.parser.BlockNode;

/**
 * @author Anton.I.Neverov
 */
public class BlockNodeVisitor extends NodeVisitor<BlockNode> {
    @Override
    protected StuffToCompare createStuffToCompare(BlockNode left, BlockNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.attributes);
        stuffToCompare.rightChildren.add(right.attributes);

        stuffToCompare.leftChildren.add(left.statements);
        stuffToCompare.rightChildren.add(right.statements);

        return stuffToCompare;
    }
}
