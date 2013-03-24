package codeOrchestra.tree.visitor;

import macromedia.asc.parser.SwitchStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class SwitchStatementNodeVisitor extends NodeVisitor<SwitchStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(SwitchStatementNode left, SwitchStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
