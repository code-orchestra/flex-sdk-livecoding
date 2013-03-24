package codeOrchestra.tree.visitor;

import macromedia.asc.parser.FinallyClauseNode;

/**
 * @author Anton.I.Neverov
 */
public class FinallyClauseNodeVisitor extends NodeVisitor<FinallyClauseNode> {
    @Override
    protected StuffToCompare createStuffToCompare(FinallyClauseNode left, FinallyClauseNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.statements);
        stuffToCompare.rightChildren.add(right.statements);

        stuffToCompare.leftChildren.add(left.default_catch);
        stuffToCompare.rightChildren.add(right.default_catch);

        return stuffToCompare;
    }
}
