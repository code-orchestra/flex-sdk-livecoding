package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ListNode;

/**
 * @author Anton.I.Neverov
 */
public class ListNodeVisitor<N extends ListNode> extends NodeVisitor<N> {
    @Override
    protected StuffToCompare createStuffToCompare(ListNode left, ListNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.addAll(left.items);
        stuffToCompare.rightChildren.addAll(right.items);

        stuffToCompare.leftLeaves.addAll(left.values);
        stuffToCompare.rightLeaves.addAll(right.values);

        return stuffToCompare;
    }
}
