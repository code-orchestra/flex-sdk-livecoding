package codeOrchestra.tree.visitor;

import macromedia.asc.parser.UseRoundingNode;

/**
 * @author Anton.I.Neverov
 */
public class UseRoundingNodeVisitor extends UsePragmaNodeVisitor<UseRoundingNode> {
    @Override
    protected StuffToCompare createStuffToCompare(UseRoundingNode left, UseRoundingNode right) {
        StuffToCompare stuffToCompare = super.createStuffToCompare(left, right);

        stuffToCompare.leftLeaves.add(left.mode);
        stuffToCompare.rightLeaves.add(right.mode);

        return stuffToCompare;
    }
}
