package codeOrchestra.tree.visitor;

import macromedia.asc.parser.InheritanceNode;

/**
 * @author Anton.I.Neverov
 */
public class InheritanceNodeVisitor extends NodeVisitor<InheritanceNode> {
    @Override
    protected StuffToCompare createStuffToCompare(InheritanceNode left, InheritanceNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
