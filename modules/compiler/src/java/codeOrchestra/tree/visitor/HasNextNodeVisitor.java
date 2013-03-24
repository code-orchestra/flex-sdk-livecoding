package codeOrchestra.tree.visitor;

import macromedia.asc.parser.HasNextNode;

/**
 * @author Anton.I.Neverov
 */
public class HasNextNodeVisitor extends NodeVisitor<HasNextNode> {
    @Override
    protected StuffToCompare createStuffToCompare(HasNextNode left, HasNextNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.indexRegister);
        stuffToCompare.rightChildren.add(right.indexRegister);

        stuffToCompare.leftChildren.add(left.objectRegister);
        stuffToCompare.rightChildren.add(right.objectRegister);

        return stuffToCompare;
    }
}
