package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralNumberNode;

/**
 * @author Anton.I.Neverov
 */
public class LiteralNumberNodeVisitor extends NodeVisitor<LiteralNumberNode> {
    @Override
    protected StuffToCompare createStuffToCompare(LiteralNumberNode left, LiteralNumberNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftLeaves.add(left.type);
        stuffToCompare.rightLeaves.add(right.type);

        stuffToCompare.leftLeaves.add(left.value);
        stuffToCompare.rightLeaves.add(right.value);

        stuffToCompare.leftLeaves.add(left.numericValue);
        stuffToCompare.rightLeaves.add(right.numericValue);

        stuffToCompare.leftLeaves.add(left.numberUsage);
        stuffToCompare.rightLeaves.add(right.numberUsage);

        return stuffToCompare;
    }
}
