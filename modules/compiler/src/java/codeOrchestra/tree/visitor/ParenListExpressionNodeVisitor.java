package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ParenListExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class ParenListExpressionNodeVisitor extends NodeVisitor<ParenListExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ParenListExpressionNode left, ParenListExpressionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
