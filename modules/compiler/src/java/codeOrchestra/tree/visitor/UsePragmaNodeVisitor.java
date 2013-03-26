package codeOrchestra.tree.visitor;

import macromedia.asc.parser.UsePragmaNode;

/**
 * @author Anton.I.Neverov
 */
public class UsePragmaNodeVisitor<N extends UsePragmaNode> extends NodeVisitor<N> {
    @Override
    protected StuffToCompare createStuffToCompare(UsePragmaNode left, UsePragmaNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.identifier);
        stuffToCompare.rightChildren.add(right.identifier);

        stuffToCompare.leftChildren.add(left.argument);
        stuffToCompare.rightChildren.add(right.argument);

        return stuffToCompare;
    }
}
