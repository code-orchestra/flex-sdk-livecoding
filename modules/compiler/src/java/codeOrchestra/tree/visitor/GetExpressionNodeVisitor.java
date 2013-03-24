package codeOrchestra.tree.visitor;

import macromedia.asc.parser.GetExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class GetExpressionNodeVisitor extends NodeVisitor<GetExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(GetExpressionNode left, GetExpressionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
