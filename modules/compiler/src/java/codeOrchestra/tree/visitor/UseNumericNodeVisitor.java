package codeOrchestra.tree.visitor;

import macromedia.asc.parser.UseNumericNode;

/**
 * @author Anton.I.Neverov
 */
public class UseNumericNodeVisitor extends UsePragmaNodeVisitor<UseNumericNode> {
    @Override
    protected StuffToCompare createStuffToCompare(UseNumericNode left, UseNumericNode right) {
        StuffToCompare stuffToCompare = super.createStuffToCompare(left, right);

        stuffToCompare.leftLeaves.add(left.numeric_mode);
        stuffToCompare.rightLeaves.add(right.numeric_mode);

        return stuffToCompare;
    }
}
