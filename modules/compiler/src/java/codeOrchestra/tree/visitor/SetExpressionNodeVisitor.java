package codeOrchestra.tree.visitor;

import macromedia.asc.parser.SetExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class SetExpressionNodeVisitor extends SelectorNodeVisitor<SetExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(SetExpressionNode left, SetExpressionNode right) {
        StuffToCompare stuffToCompare = super.createStuffToCompare(left, right);

        stuffToCompare.leftChildren.add(left.args);
        stuffToCompare.rightChildren.add(right.args);

        stuffToCompare.leftLeaves.add(left.value_type);
        stuffToCompare.rightLeaves.add(right.value_type);

        stuffToCompare.leftLeaves.add(left.is_constinit);
        stuffToCompare.rightLeaves.add(right.is_constinit);

        stuffToCompare.leftLeaves.add(left.is_initializer);
        stuffToCompare.rightLeaves.add(right.is_initializer);

        return stuffToCompare;
    }
}
