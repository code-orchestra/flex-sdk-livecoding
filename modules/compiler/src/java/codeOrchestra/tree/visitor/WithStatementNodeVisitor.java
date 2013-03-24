package codeOrchestra.tree.visitor;

import macromedia.asc.parser.WithStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class WithStatementNodeVisitor extends NodeVisitor<WithStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(WithStatementNode left, WithStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
