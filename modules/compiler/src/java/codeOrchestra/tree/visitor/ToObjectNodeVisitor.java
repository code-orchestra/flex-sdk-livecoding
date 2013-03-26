package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ToObjectNode;

/**
 * @author Anton.I.Neverov
 */
public class ToObjectNodeVisitor extends NodeVisitor<ToObjectNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ToObjectNode left, ToObjectNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.expr);
        stuffToCompare.rightChildren.add(right.expr);

        return stuffToCompare;
    }
}
