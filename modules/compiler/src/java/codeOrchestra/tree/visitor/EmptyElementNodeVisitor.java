package codeOrchestra.tree.visitor;

import macromedia.asc.parser.EmptyElementNode;

/**
 * @author Anton.I.Neverov
 */
public class EmptyElementNodeVisitor extends NodeVisitor<EmptyElementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(EmptyElementNode left, EmptyElementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
