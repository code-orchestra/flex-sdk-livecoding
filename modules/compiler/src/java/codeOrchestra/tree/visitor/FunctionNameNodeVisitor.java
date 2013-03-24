package codeOrchestra.tree.visitor;

import macromedia.asc.parser.FunctionNameNode;

/**
 * @author Anton.I.Neverov
 */
public class FunctionNameNodeVisitor extends NodeVisitor<FunctionNameNode> {
    @Override
    protected StuffToCompare createStuffToCompare(FunctionNameNode left, FunctionNameNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.identifier);
        stuffToCompare.rightChildren.add(right.identifier);

        stuffToCompare.leftLeaves.add(left.kind);
        stuffToCompare.rightLeaves.add(right.kind);

        return stuffToCompare;
    }
}
