package codeOrchestra.tree.visitor;

import macromedia.asc.parser.FunctionSignatureNode;

/**
 * @author Anton.I.Neverov
 */
public class FunctionSignatureNodeVisitor extends NodeVisitor<FunctionSignatureNode> {
    @Override
    protected StuffToCompare createStuffToCompare(FunctionSignatureNode left, FunctionSignatureNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.parameter);
        stuffToCompare.rightChildren.add(right.parameter);

        stuffToCompare.leftChildren.add(left.result);
        stuffToCompare.rightChildren.add(right.result);

        // TODO inits ?

        stuffToCompare.leftLeaves.add(left.type);
        stuffToCompare.rightLeaves.add(right.type);

        stuffToCompare.leftLeaves.add(left.typeref);
        stuffToCompare.rightLeaves.add(right.typeref);

        return stuffToCompare;
    }
}
