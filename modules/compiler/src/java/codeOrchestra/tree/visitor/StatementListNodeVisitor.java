package codeOrchestra.tree.visitor;

import macromedia.asc.parser.StatementListNode;

/**
 * @author Anton.I.Neverov
 */
public class StatementListNodeVisitor extends NodeVisitor<StatementListNode> {
    @Override
    protected StuffToCompare createStuffToCompare(StatementListNode left, StatementListNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.addAll(left.items);
        stuffToCompare.rightChildren.addAll(right.items);

        stuffToCompare.leftChildren.add(left.config_attrs);
        stuffToCompare.rightChildren.add(right.config_attrs);

        stuffToCompare.leftLeaves.add(left.dominates_program_endpoint);
        stuffToCompare.rightLeaves.add(right.dominates_program_endpoint);

        stuffToCompare.leftLeaves.add(left.was_empty);
        stuffToCompare.rightLeaves.add(right.was_empty);

        stuffToCompare.leftLeaves.add(left.is_loop);
        stuffToCompare.rightLeaves.add(right.is_loop);

        stuffToCompare.leftLeaves.add(left.is_block);
        stuffToCompare.rightLeaves.add(right.is_block);

        stuffToCompare.leftLeaves.add(left.has_pragma);
        stuffToCompare.rightLeaves.add(right.has_pragma);

        stuffToCompare.leftLeaves.add(left.numberUsage);
        stuffToCompare.rightLeaves.add(right.numberUsage);

        stuffToCompare.leftLeaves.add(left.default_namespace);
        stuffToCompare.rightLeaves.add(right.default_namespace);

        return stuffToCompare;
    }
}
