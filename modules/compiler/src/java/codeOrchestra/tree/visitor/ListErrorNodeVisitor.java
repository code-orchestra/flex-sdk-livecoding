package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ListErrorNode;

/**
 * @author Anton.I.Neverov
 */
public class ListErrorNodeVisitor extends ListNodeVisitor<ListErrorNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ListErrorNode left, ListErrorNode right) {
        StuffToCompare stuffToCompare = super.createStuffToCompare(left, right);

        stuffToCompare.leftLeaves.add(left.value);
        stuffToCompare.rightLeaves.add(right.value);

        return stuffToCompare;
    }
}
