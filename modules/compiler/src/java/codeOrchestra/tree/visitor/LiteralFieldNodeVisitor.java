package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralFieldNode;

/**
 * @author Anton.I.Neverov
 */
public class LiteralFieldNodeVisitor extends NodeVisitor<LiteralFieldNode> {
    @Override
    protected StuffToCompare createStuffToCompare(LiteralFieldNode left, LiteralFieldNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.name);
        stuffToCompare.rightChildren.add(right.name);

        stuffToCompare.leftChildren.add(left.value);
        stuffToCompare.rightChildren.add(right.value);

        stuffToCompare.leftLeaves.add(left.ref);
        stuffToCompare.rightLeaves.add(right.ref);

        return stuffToCompare;
    }
}
