package codeOrchestra.tree.visitor;

import macromedia.asc.parser.TryStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class TryStatementNodeVisitor extends NodeVisitor<TryStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(TryStatementNode left, TryStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
