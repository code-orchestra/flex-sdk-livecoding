package codeOrchestra.tree.visitor;

import macromedia.asc.parser.IdentifierErrorNode;

/**
 * @author Anton.I.Neverov
 */
public class IdentifierErrorNodeVisitor extends IdentifierNodeVisitor<IdentifierErrorNode> {
    @Override
    protected StuffToCompare createStuffToCompare(IdentifierErrorNode left, IdentifierErrorNode right) {
        StuffToCompare stuffToCompare = super.createStuffToCompare(left, right);

        stuffToCompare.leftLeaves.add(left.value);
        stuffToCompare.rightLeaves.add(right.value);

        return stuffToCompare;
    }
}
