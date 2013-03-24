package codeOrchestra.tree.visitor;

import macromedia.asc.parser.CaseLabelNode;

/**
 * @author Anton.I.Neverov
 */
public class CaseLabelNodeVisitor extends NodeVisitor<CaseLabelNode> {
    @Override
    protected StuffToCompare createStuffToCompare(CaseLabelNode left, CaseLabelNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.label);
        stuffToCompare.rightChildren.add(right.label);

        return stuffToCompare;
    }
}
