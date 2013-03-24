package codeOrchestra.tree.visitor;

import macromedia.asc.parser.VariableDefinitionNode;

/**
 * @author Anton.I.Neverov
 */
public class VariableDefinitionNodeVisitor<N extends VariableDefinitionNode> extends DefinitionNodeVisitor<N> {
    @Override
    protected StuffToCompare createStuffToCompare(VariableDefinitionNode left, VariableDefinitionNode right) {
        StuffToCompare stuffToCompare = super.createStuffToCompare(left, right);

        stuffToCompare.leftChildren.add(left.list);
        stuffToCompare.rightChildren.add(right.list);

        stuffToCompare.leftLeaves.add(left.kind);
        stuffToCompare.rightLeaves.add(right.kind);

        return stuffToCompare;
    }
}
