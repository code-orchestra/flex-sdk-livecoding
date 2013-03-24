package codeOrchestra.tree.visitor;

import macromedia.asc.parser.CatchClauseNode;

/**
 * @author Anton.I.Neverov
 */
public class CatchClauseNodeVisitor extends NodeVisitor<CatchClauseNode> {
    @Override
    protected StuffToCompare createStuffToCompare(CatchClauseNode left, CatchClauseNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.parameter);
        stuffToCompare.rightChildren.add(right.parameter);

        stuffToCompare.leftChildren.add(left.statements);
        stuffToCompare.rightChildren.add(right.statements);

        stuffToCompare.leftLeaves.add(left.typeref);
        stuffToCompare.rightLeaves.add(right.typeref);

        stuffToCompare.leftLeaves.add(left.finallyInserted);
        stuffToCompare.rightLeaves.add(right.finallyInserted);

        stuffToCompare.leftLeaves.add(left.default_namespace);
        stuffToCompare.rightLeaves.add(right.default_namespace);

        stuffToCompare.leftLeaves.add(left.activation);
        stuffToCompare.rightLeaves.add(right.activation);

        return stuffToCompare;
    }
}
