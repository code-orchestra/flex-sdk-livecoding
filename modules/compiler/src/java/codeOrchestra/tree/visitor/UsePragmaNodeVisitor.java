package codeOrchestra.tree.visitor;

import macromedia.asc.parser.UsePragmaNode;

/**
 * @author Anton.I.Neverov
 */
public class UsePragmaNodeVisitor extends NodeVisitor<UsePragmaNode> {
    @Override
    protected StuffToCompare createStuffToCompare(UsePragmaNode left, UsePragmaNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
