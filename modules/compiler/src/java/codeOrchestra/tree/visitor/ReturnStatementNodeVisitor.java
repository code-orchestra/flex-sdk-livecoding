package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ReturnStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class ReturnStatementNodeVisitor extends NodeVisitor<ReturnStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ReturnStatementNode left, ReturnStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
