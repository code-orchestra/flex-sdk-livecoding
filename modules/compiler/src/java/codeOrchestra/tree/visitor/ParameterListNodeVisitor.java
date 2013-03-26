package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ParameterListNode;

/**
 * @author Anton.I.Neverov
 */
public class ParameterListNodeVisitor extends NodeVisitor<ParameterListNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ParameterListNode left, ParameterListNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.addAll(left.items);
        stuffToCompare.rightChildren.addAll(right.items);

        stuffToCompare.leftLeaves.addAll(left.types);
        stuffToCompare.rightLeaves.addAll(right.types);

        stuffToCompare.leftLeaves.add(left.decl_styles);
        stuffToCompare.rightLeaves.add(right.decl_styles);

        return stuffToCompare;
    }
}
