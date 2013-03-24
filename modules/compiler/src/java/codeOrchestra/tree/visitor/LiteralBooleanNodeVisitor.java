package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralBooleanNode;

/**
 * @author Anton.I.Neverov
 */
public class LiteralBooleanNodeVisitor extends NodeVisitor<LiteralBooleanNode> {
    @Override
    protected StuffToCompare createStuffToCompare(LiteralBooleanNode left, LiteralBooleanNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftLeaves.add(left.value);
        stuffToCompare.rightLeaves.add(right.value);

        return stuffToCompare;
    }
}
