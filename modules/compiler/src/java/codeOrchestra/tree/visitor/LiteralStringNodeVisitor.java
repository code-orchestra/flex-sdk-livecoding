package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralStringNode;

/**
 * @author Anton.I.Neverov
 */
public class LiteralStringNodeVisitor extends NodeVisitor<LiteralStringNode> {
    @Override
    protected StuffToCompare createStuffToCompare(LiteralStringNode left, LiteralStringNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftLeaves.add(left.value);
        stuffToCompare.rightLeaves.add(right.value);

        return stuffToCompare;
    }
}
