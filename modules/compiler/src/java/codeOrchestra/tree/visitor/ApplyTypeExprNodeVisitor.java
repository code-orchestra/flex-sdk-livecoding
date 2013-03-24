package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ApplyTypeExprNode;

/**
 * @author Anton.I.Neverov
 */
public class ApplyTypeExprNodeVisitor extends NodeVisitor<ApplyTypeExprNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ApplyTypeExprNode left, ApplyTypeExprNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
