package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralNullNode;

/**
 * @author Anton.I.Neverov
 */
public class LiteralNullNodeVisitor extends NodeVisitor<LiteralNullNode> {
    @Override
    protected StuffToCompare createStuffToCompare(LiteralNullNode left, LiteralNullNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
