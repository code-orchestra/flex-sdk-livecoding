package codeOrchestra.tree.visitor;

import macromedia.asc.parser.TypeIdentifierNode;

/**
 * @author Anton.I.Neverov
 */
public class TypeIdentifierNodeVisitor extends IdentifierNodeVisitor<TypeIdentifierNode> {
    @Override
    protected StuffToCompare createStuffToCompare(TypeIdentifierNode left, TypeIdentifierNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.base);
        stuffToCompare.rightChildren.add(right.base);

        stuffToCompare.leftChildren.add(left.typeArgs);
        stuffToCompare.rightChildren.add(right.typeArgs);

        return stuffToCompare;
    }
}
