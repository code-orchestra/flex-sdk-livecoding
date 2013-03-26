package codeOrchestra.tree.visitor;

import macromedia.asc.parser.DocCommentNode;

/**
 * @author Anton.I.Neverov
 */
public class DocCommentNodeVisitor extends MetaDataNodeVisitor<DocCommentNode> {
    @Override
    protected StuffToCompare createStuffToCompare(DocCommentNode left, DocCommentNode right) {
        StuffToCompare stuffToCompare = super.createStuffToCompare(left, right);

        stuffToCompare.leftChildren.add(left.metaData);
        stuffToCompare.rightChildren.add(right.metaData);

        stuffToCompare.leftLeaves.add(left.is_default);
        stuffToCompare.rightLeaves.add(right.is_default);

        return stuffToCompare;
    }
}
