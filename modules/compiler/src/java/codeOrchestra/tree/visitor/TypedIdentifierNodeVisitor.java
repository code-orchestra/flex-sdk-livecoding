package codeOrchestra.tree.visitor;

import macromedia.asc.parser.TypedIdentifierNode;

/**
 * @author Anton.I.Neverov
 */
public class TypedIdentifierNodeVisitor extends NodeVisitor<TypedIdentifierNode> {
    @Override
    protected StuffToCompare createStuffToCompare(TypedIdentifierNode left, TypedIdentifierNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.identifier);
        stuffToCompare.rightChildren.add(right.identifier);

        stuffToCompare.leftChildren.add(left.type);
        stuffToCompare.rightChildren.add(right.type);

        stuffToCompare.leftLeaves.add(left.no_anno);
        stuffToCompare.rightLeaves.add(right.no_anno);

        return stuffToCompare;
    }
}
