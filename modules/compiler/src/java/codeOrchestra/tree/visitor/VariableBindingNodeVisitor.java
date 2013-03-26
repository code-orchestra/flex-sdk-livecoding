package codeOrchestra.tree.visitor;

import macromedia.asc.parser.VariableBindingNode;

/**
 * @author Anton.I.Neverov
 */
public class VariableBindingNodeVisitor extends NodeVisitor<VariableBindingNode> {
    @Override
    protected StuffToCompare createStuffToCompare(VariableBindingNode left, VariableBindingNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.variable);
        stuffToCompare.rightChildren.add(right.variable);

        stuffToCompare.leftChildren.add(left.initializer);
        stuffToCompare.rightChildren.add(right.initializer);

        stuffToCompare.leftChildren.add(left.attrs);
        stuffToCompare.rightChildren.add(right.attrs);

        stuffToCompare.leftLeaves.add(left.ref);
        stuffToCompare.rightLeaves.add(right.ref);

        stuffToCompare.leftLeaves.add(left.typeref);
        stuffToCompare.rightLeaves.add(right.typeref);

        stuffToCompare.leftLeaves.add(left.debug_name);
        stuffToCompare.rightLeaves.add(right.debug_name);

        stuffToCompare.leftLeaves.add(left.kind);
        stuffToCompare.rightLeaves.add(right.kind);

        return stuffToCompare;
    }
}
