package codeOrchestra.tree.visitor;

import macromedia.asc.parser.IncrementNode;

/**
 * @author Anton.I.Neverov
 */
public class IncrementNodeVisitor extends NodeVisitor<IncrementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(IncrementNode left, IncrementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
