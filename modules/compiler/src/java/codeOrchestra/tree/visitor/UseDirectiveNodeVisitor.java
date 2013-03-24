package codeOrchestra.tree.visitor;

import macromedia.asc.parser.UseDirectiveNode;

/**
 * @author Anton.I.Neverov
 */
public class UseDirectiveNodeVisitor extends NodeVisitor<UseDirectiveNode> {
    @Override
    protected StuffToCompare createStuffToCompare(UseDirectiveNode left, UseDirectiveNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
