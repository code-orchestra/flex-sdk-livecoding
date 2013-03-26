package codeOrchestra.tree.visitor;

import macromedia.asc.parser.PragmaExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class PragmaExpressionNodeVisitor extends NodeVisitor<PragmaExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(PragmaExpressionNode left, PragmaExpressionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.identifier);
        stuffToCompare.rightChildren.add(right.identifier);

        stuffToCompare.leftChildren.add(left.arg);
        stuffToCompare.rightChildren.add(right.arg);

        return stuffToCompare;
    }
}
