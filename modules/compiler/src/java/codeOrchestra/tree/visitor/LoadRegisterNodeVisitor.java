package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LoadRegisterNode;

/**
 * @author Anton.I.Neverov
 */
public class LoadRegisterNodeVisitor extends NodeVisitor<LoadRegisterNode> {
    @Override
    protected StuffToCompare createStuffToCompare(LoadRegisterNode left, LoadRegisterNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.reg);
        stuffToCompare.rightChildren.add(right.reg);

        stuffToCompare.leftLeaves.add(left.type);
        stuffToCompare.rightLeaves.add(right.type);

        return stuffToCompare;
    }
}
