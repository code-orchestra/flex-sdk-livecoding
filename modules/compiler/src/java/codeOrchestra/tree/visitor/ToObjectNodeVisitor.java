package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ToObjectNode;

/**
 * @author Anton.I.Neverov
 */
public class ToObjectNodeVisitor extends NodeVisitor<ToObjectNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ToObjectNode left, ToObjectNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
