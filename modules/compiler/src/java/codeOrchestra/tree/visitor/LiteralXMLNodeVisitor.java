package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralXMLNode;

/**
 * @author Anton.I.Neverov
 */
public class LiteralXMLNodeVisitor extends NodeVisitor<LiteralXMLNode> {
    @Override
    protected StuffToCompare createStuffToCompare(LiteralXMLNode left, LiteralXMLNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.list);
        stuffToCompare.rightChildren.add(right.list);

        stuffToCompare.leftLeaves.add(left.is_xmllist);
        stuffToCompare.rightLeaves.add(right.is_xmllist);

        return stuffToCompare;
    }
}
