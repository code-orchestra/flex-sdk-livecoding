package codeOrchestra.tree.visitor;

import macromedia.asc.parser.StoreRegisterNode;

/**
 * @author Anton.I.Neverov
 */
public class StoreRegisterNodeVisitor extends NodeVisitor<StoreRegisterNode> {
    @Override
    protected StuffToCompare createStuffToCompare(StoreRegisterNode left, StoreRegisterNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
