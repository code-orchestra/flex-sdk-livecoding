package codeOrchestra.tree.visitor;

import macromedia.asc.parser.CallExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class CallExpressionNodeVisitor extends NodeVisitor<CallExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(CallExpressionNode left, CallExpressionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
