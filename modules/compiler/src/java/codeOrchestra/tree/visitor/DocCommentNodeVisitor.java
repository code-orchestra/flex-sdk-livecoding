package codeOrchestra.tree.visitor;

import macromedia.asc.parser.DocCommentNode;

/**
 * @author Anton.I.Neverov
 */
public class DocCommentNodeVisitor extends NodeVisitor<DocCommentNode> {
    @Override
    protected StuffToCompare createStuffToCompare(DocCommentNode left, DocCommentNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
