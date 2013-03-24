package codeOrchestra.tree.visitor;

import macromedia.asc.parser.StatementListNode;

/**
 * @author Anton.I.Neverov
 */
public class StatementListNodeVisitor extends NodeVisitor<StatementListNode> {
    @Override
    protected StuffToCompare createStuffToCompare(StatementListNode left, StatementListNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
