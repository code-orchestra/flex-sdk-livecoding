package codeOrchestra.tree.visitor;

import macromedia.asc.parser.EmptyStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class EmptyStatementNodeVisitor extends NodeVisitor<EmptyStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(EmptyStatementNode left, EmptyStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
