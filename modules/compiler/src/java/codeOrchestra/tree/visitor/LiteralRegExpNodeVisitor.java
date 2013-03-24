package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralRegExpNode;

/**
 * @author Anton.I.Neverov
 */
public class LiteralRegExpNodeVisitor extends NodeVisitor<LiteralRegExpNode> {
    @Override
    protected StuffToCompare createStuffToCompare(LiteralRegExpNode left, LiteralRegExpNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftLeaves.add(left.value);
        stuffToCompare.rightLeaves.add(right.value);

        return stuffToCompare;
    }
}
