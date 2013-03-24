package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralArrayNode;

/**
 * @author Anton.I.Neverov
 */
public class LiteralArrayNodeVisitor extends NodeVisitor<LiteralArrayNode> {
    @Override
    protected StuffToCompare createStuffToCompare(LiteralArrayNode left, LiteralArrayNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.elementlist);
        stuffToCompare.rightChildren.add(right.elementlist);

        stuffToCompare.leftLeaves.add(left.value);
        stuffToCompare.rightLeaves.add(right.value);

        return stuffToCompare;
    }
}
