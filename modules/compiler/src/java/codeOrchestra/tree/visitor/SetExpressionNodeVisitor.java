package codeOrchestra.tree.visitor;

import macromedia.asc.parser.SetExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class SetExpressionNodeVisitor extends NodeVisitor<SetExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(SetExpressionNode left, SetExpressionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
