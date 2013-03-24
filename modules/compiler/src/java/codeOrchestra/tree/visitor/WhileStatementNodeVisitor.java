package codeOrchestra.tree.visitor;

import macromedia.asc.parser.WhileStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class WhileStatementNodeVisitor extends NodeVisitor<WhileStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(WhileStatementNode left, WhileStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
