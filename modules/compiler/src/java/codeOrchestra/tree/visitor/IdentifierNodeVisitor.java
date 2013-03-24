package codeOrchestra.tree.visitor;

import macromedia.asc.parser.IdentifierNode;

/**
 * @author Anton.I.Neverov
 */
public class IdentifierNodeVisitor<N extends IdentifierNode> extends NodeVisitor<N> {
    @Override
    protected StuffToCompare createStuffToCompare(IdentifierNode left, IdentifierNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftLeaves.add(left.name);
        stuffToCompare.rightLeaves.add(right.name);

        stuffToCompare.leftLeaves.add(left.ref);
        stuffToCompare.rightLeaves.add(right.ref);

        return stuffToCompare;
    }
}
