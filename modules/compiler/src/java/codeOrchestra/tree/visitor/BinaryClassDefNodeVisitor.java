package codeOrchestra.tree.visitor;

import macromedia.asc.parser.BinaryClassDefNode;

/**
 * @author Anton.I.Neverov
 */
public class BinaryClassDefNodeVisitor extends NodeVisitor<BinaryClassDefNode> {
    @Override
    protected StuffToCompare createStuffToCompare(BinaryClassDefNode left, BinaryClassDefNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
