package codeOrchestra.tree.visitor;

import macromedia.asc.parser.PragmaNode;

/**
 * @author Anton.I.Neverov
 */
public class PragmaNodeVisitor extends NodeVisitor<PragmaNode> {
    @Override
    protected StuffToCompare createStuffToCompare(PragmaNode left, PragmaNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.list);
        stuffToCompare.rightChildren.add(right.list);

        return stuffToCompare;
    }
}
