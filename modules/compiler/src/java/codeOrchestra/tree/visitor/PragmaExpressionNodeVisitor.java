package codeOrchestra.tree.visitor;

import macromedia.asc.parser.PragmaExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class PragmaExpressionNodeVisitor extends NodeVisitor<PragmaExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(PragmaExpressionNode left, PragmaExpressionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
