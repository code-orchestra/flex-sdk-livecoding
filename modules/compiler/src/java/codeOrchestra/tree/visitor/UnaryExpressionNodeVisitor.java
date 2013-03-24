package codeOrchestra.tree.visitor;

import macromedia.asc.parser.UnaryExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class UnaryExpressionNodeVisitor extends NodeVisitor<UnaryExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(UnaryExpressionNode left, UnaryExpressionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
