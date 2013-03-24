package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralObjectNode;

/**
 * @author Anton.I.Neverov
 */
public class LiteralObjectNodeVisitor extends NodeVisitor<LiteralObjectNode> {
    @Override
    protected StuffToCompare createStuffToCompare(LiteralObjectNode left, LiteralObjectNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.fieldlist);
        stuffToCompare.rightChildren.add(right.fieldlist);

        return stuffToCompare;
    }
}
