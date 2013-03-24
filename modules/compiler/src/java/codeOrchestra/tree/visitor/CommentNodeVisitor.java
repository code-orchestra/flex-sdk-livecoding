package codeOrchestra.tree.visitor;

import macromedia.asc.parser.CommentNode;

/**
 * @author Anton.I.Neverov
 */
public class CommentNodeVisitor extends NodeVisitor<CommentNode> {
    @Override
    protected StuffToCompare createStuffToCompare(CommentNode left, CommentNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftLeaves.add(left.getType());
        stuffToCompare.rightLeaves.add(right.getType());

        stuffToCompare.leftLeaves.add(left.toString());
        stuffToCompare.rightLeaves.add(right.toString());

        return stuffToCompare;
    }
}
