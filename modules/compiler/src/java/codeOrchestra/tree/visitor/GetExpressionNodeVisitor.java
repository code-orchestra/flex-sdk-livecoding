package codeOrchestra.tree.visitor;

import macromedia.asc.parser.GetExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class GetExpressionNodeVisitor extends SelectorNodeVisitor<GetExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(GetExpressionNode left, GetExpressionNode right) {
        StuffToCompare stuffToCompare = super.createStuffToCompare(left, right);

		// Nothing here

        return stuffToCompare;
    }
}
