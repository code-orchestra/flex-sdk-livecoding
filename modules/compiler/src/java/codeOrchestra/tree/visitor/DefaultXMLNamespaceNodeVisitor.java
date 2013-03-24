package codeOrchestra.tree.visitor;

import macromedia.asc.parser.DefaultXMLNamespaceNode;

/**
 * @author Anton.I.Neverov
 */
public class DefaultXMLNamespaceNodeVisitor extends NodeVisitor<DefaultXMLNamespaceNode> {
    @Override
    protected StuffToCompare createStuffToCompare(DefaultXMLNamespaceNode left, DefaultXMLNamespaceNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.expr);
        stuffToCompare.rightChildren.add(right.expr);

        stuffToCompare.leftLeaves.add(left.ref);
        stuffToCompare.rightLeaves.add(right.ref);

        return stuffToCompare;
    }
}
