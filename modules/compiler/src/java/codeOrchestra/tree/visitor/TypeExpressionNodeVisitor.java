package codeOrchestra.tree.visitor;

import macromedia.asc.parser.TypeExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class TypeExpressionNodeVisitor extends NodeVisitor<TypeExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(TypeExpressionNode left, TypeExpressionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.expr);
        stuffToCompare.rightChildren.add(right.expr);

        stuffToCompare.leftLeaves.add(left.nullable_annotation);
        stuffToCompare.rightLeaves.add(right.nullable_annotation);

        stuffToCompare.leftLeaves.add(left.is_nullable);
        stuffToCompare.rightLeaves.add(right.is_nullable);

        return stuffToCompare;
    }
}
