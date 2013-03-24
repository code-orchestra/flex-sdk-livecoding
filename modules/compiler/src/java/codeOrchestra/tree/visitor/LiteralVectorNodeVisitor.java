package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralVectorNode;

/**
 * @author Anton.I.Neverov
 */
public class LiteralVectorNodeVisitor extends NodeVisitor<LiteralVectorNode> {
    @Override
    protected StuffToCompare createStuffToCompare(LiteralVectorNode left, LiteralVectorNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.type);
        stuffToCompare.rightChildren.add(right.type);

        stuffToCompare.leftChildren.add(left.elementlist);
        stuffToCompare.rightChildren.add(right.elementlist);

        return stuffToCompare;
    }
}
