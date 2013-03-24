package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ErrorNode;

/**
 * @author Anton.I.Neverov
 */
public class ErrorNodeVisitor extends NodeVisitor<ErrorNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ErrorNode left, ErrorNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftLeaves.add(left.errorArg);
        stuffToCompare.rightLeaves.add(right.errorArg);

        stuffToCompare.leftLeaves.add(left.errorCode);
        stuffToCompare.rightLeaves.add(right.errorCode);

        return stuffToCompare;
    }
}
