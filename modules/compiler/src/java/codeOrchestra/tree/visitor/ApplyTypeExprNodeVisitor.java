package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ApplyTypeExprNode;

/**
 * @author Anton.I.Neverov
 */
public class ApplyTypeExprNodeVisitor extends SelectorNodeVisitor<ApplyTypeExprNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ApplyTypeExprNode left, ApplyTypeExprNode right) {
        StuffToCompare stuffToCompare = super.createStuffToCompare(left, right);

        stuffToCompare.leftChildren.add(left.typeArgs);
        stuffToCompare.rightChildren.add(right.typeArgs);

        return stuffToCompare;
    }
}
