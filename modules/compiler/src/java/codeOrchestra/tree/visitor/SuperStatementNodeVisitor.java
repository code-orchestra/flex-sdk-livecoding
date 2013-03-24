package codeOrchestra.tree.visitor;

import macromedia.asc.parser.SuperStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class SuperStatementNodeVisitor extends NodeVisitor<SuperStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(SuperStatementNode left, SuperStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
