package codeOrchestra.tree.visitor;

import macromedia.asc.parser.UseRoundingNode;

/**
 * @author Anton.I.Neverov
 */
public class UseRoundingNodeVisitor extends NodeVisitor<UseRoundingNode> {
    @Override
    protected StuffToCompare createStuffToCompare(UseRoundingNode left, UseRoundingNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
