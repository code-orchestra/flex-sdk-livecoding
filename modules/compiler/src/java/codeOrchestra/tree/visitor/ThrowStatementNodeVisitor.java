package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ThrowStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class ThrowStatementNodeVisitor extends NodeVisitor<ThrowStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ThrowStatementNode left, ThrowStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
