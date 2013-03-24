package codeOrchestra.tree.visitor;

import macromedia.asc.parser.FunctionCommonNode;

/**
 * @author Anton.I.Neverov
 */
public class FunctionCommonNodeVisitor extends NodeVisitor<FunctionCommonNode> {
    @Override
    protected StuffToCompare createStuffToCompare(FunctionCommonNode left, FunctionCommonNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.identifier);
        stuffToCompare.rightChildren.add(right.identifier);

        stuffToCompare.leftChildren.add(left.signature);
        stuffToCompare.rightChildren.add(right.signature);

        stuffToCompare.leftChildren.add(left.body);
        stuffToCompare.rightChildren.add(right.body);

        stuffToCompare.leftLeaves.add(left.kind);
        stuffToCompare.rightLeaves.add(right.kind);

        // TODO: Other fields?

        return stuffToCompare;
    }
}
