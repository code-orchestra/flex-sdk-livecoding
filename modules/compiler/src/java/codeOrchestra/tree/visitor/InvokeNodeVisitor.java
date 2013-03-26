package codeOrchestra.tree.visitor;

import macromedia.asc.parser.InvokeNode;

/**
 * @author Anton.I.Neverov
 */
public class InvokeNodeVisitor extends SelectorNodeVisitor<InvokeNode> {
    @Override
    protected StuffToCompare createStuffToCompare(InvokeNode left, InvokeNode right) {
        StuffToCompare stuffToCompare = super.createStuffToCompare(left, right);

        stuffToCompare.leftChildren.add(left.args);
        stuffToCompare.rightChildren.add(right.args);

        stuffToCompare.leftLeaves.add(left.name);
        stuffToCompare.rightLeaves.add(right.name);

        stuffToCompare.leftLeaves.add(left.index);
        stuffToCompare.rightLeaves.add(right.index);

        return stuffToCompare;
    }
}
