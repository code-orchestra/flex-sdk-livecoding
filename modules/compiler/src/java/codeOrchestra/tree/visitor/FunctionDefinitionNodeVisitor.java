package codeOrchestra.tree.visitor;

import macromedia.asc.parser.FunctionDefinitionNode;

/**
 * @author Anton.I.Neverov
 */
public class FunctionDefinitionNodeVisitor<N extends FunctionDefinitionNode> extends DefinitionNodeVisitor<N> {
    @Override
    protected StuffToCompare createStuffToCompare(FunctionDefinitionNode left, FunctionDefinitionNode right) {
        StuffToCompare stuffToCompare = super.createStuffToCompare(left, right);

        stuffToCompare.leftChildren.add(left.name);
        stuffToCompare.rightChildren.add(right.name);

        stuffToCompare.leftChildren.add(left.fexpr);
        stuffToCompare.rightChildren.add(right.fexpr);

        stuffToCompare.leftChildren.add(left.init);
        stuffToCompare.rightChildren.add(right.init);

        stuffToCompare.leftLeaves.add(left.fun);
        stuffToCompare.rightLeaves.add(right.fun);

        // TODO: Other fields?

        return stuffToCompare;
    }
}
