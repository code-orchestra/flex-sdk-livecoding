package codeOrchestra.tree.visitor;

import macromedia.asc.parser.UseNumericNode;

/**
 * @author Anton.I.Neverov
 */
public class UseNumericNodeVisitor extends NodeVisitor<UseNumericNode> {
    @Override
    protected StuffToCompare createStuffToCompare(UseNumericNode left, UseNumericNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
