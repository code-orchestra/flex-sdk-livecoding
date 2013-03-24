package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ArgumentListNode;

/**
 * @author Anton.I.Neverov
 */
public class ArgumentListNodeVisitor extends NodeVisitor<ArgumentListNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ArgumentListNode left, ArgumentListNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.addAll(left.items);
        stuffToCompare.rightChildren.addAll(right.items);

        stuffToCompare.leftLeaves.addAll(left.expected_types);
        stuffToCompare.rightLeaves.addAll(right.expected_types);

        stuffToCompare.leftLeaves.add(left.decl_styles);
        stuffToCompare.rightLeaves.add(right.decl_styles);

        stuffToCompare.leftLeaves.add(left.is_bracket_selector);
        stuffToCompare.rightLeaves.add(right.is_bracket_selector);

        return stuffToCompare;
    }
}
