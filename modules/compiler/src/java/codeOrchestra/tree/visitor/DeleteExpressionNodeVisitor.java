package codeOrchestra.tree.visitor;

import macromedia.asc.parser.DeleteExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class DeleteExpressionNodeVisitor extends NodeVisitor<DeleteExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(DeleteExpressionNode left, DeleteExpressionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
