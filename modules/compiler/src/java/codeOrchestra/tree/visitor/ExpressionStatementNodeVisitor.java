package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ExpressionStatementNode;

/**
 * @author Anton.I.Neverov
 */
public class ExpressionStatementNodeVisitor extends NodeVisitor<ExpressionStatementNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ExpressionStatementNode left, ExpressionStatementNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.expr);
        stuffToCompare.rightChildren.add(right.expr);

        stuffToCompare.leftLeaves.add(left.gen_bits);
        stuffToCompare.rightLeaves.add(right.gen_bits);

        stuffToCompare.leftLeaves.add(left.ref);
        stuffToCompare.rightLeaves.add(right.ref);

        stuffToCompare.leftLeaves.add(left.expected_type);
        stuffToCompare.rightLeaves.add(right.expected_type);

        stuffToCompare.leftLeaves.add(left.is_var_stmt);
        stuffToCompare.rightLeaves.add(right.is_var_stmt);

        return stuffToCompare;
    }
}
