package codeOrchestra.tree.visitor;

import macromedia.asc.parser.AttributeListNode;

/**
 * @author Anton.I.Neverov
 */
public class AttributeListNodeVisitor extends NodeVisitor<AttributeListNode> {
    @Override
    protected StuffToCompare createStuffToCompare(AttributeListNode left, AttributeListNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.addAll(left.items);
        stuffToCompare.rightChildren.addAll(right.items);

        stuffToCompare.leftLeaves.addAll(left.namespaces);
        stuffToCompare.rightLeaves.addAll(right.namespaces);

        stuffToCompare.leftLeaves.addAll(left.namespace_ids);
        stuffToCompare.rightLeaves.addAll(right.namespace_ids);

        stuffToCompare.leftLeaves.add(left.getUserNamespace());
        stuffToCompare.rightLeaves.add(right.getUserNamespace());

        return stuffToCompare;
    }
}
