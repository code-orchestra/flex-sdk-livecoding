package codeOrchestra.tree.visitor;

import macromedia.asc.parser.QualifiedExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class QualifiedExpressionNodeVisitor extends QualifiedIdentifierNodeVisitor<QualifiedExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(QualifiedExpressionNode left, QualifiedExpressionNode right) {
        StuffToCompare stuffToCompare = super.createStuffToCompare(left, right);

        stuffToCompare.leftChildren.add(left.expr);
        stuffToCompare.rightChildren.add(right.expr);

        stuffToCompare.leftLeaves.addAll(left.nss);
        stuffToCompare.rightLeaves.addAll(right.nss);

        return stuffToCompare;
    }
}
