package codeOrchestra.tree.visitor;

import macromedia.asc.parser.UntypedVariableBindingNode;

/**
 * @author Anton.I.Neverov
 */
public class UntypedVariableBindingNodeVisitor extends NodeVisitor<UntypedVariableBindingNode> {
    @Override
    protected StuffToCompare createStuffToCompare(UntypedVariableBindingNode left, UntypedVariableBindingNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.identifier);
        stuffToCompare.rightChildren.add(right.identifier);

        stuffToCompare.leftChildren.add(left.initializer);
        stuffToCompare.rightChildren.add(right.initializer);

        stuffToCompare.leftLeaves.add(left.ref);
        stuffToCompare.rightLeaves.add(right.ref);

        return stuffToCompare;
    }
}
