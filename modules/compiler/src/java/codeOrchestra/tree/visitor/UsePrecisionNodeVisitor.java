package codeOrchestra.tree.visitor;

import macromedia.asc.parser.UsePrecisionNode;

/**
 * @author Anton.I.Neverov
 */
public class UsePrecisionNodeVisitor extends NodeVisitor<UsePrecisionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(UsePrecisionNode left, UsePrecisionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
