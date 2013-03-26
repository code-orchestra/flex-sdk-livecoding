package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ThisExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class ThisExpressionNodeVisitor extends NodeVisitor<ThisExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ThisExpressionNode left, ThisExpressionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		// Nothing here

        return stuffToCompare;
    }
}
