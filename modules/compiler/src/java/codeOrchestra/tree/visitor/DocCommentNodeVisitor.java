package codeOrchestra.tree.visitor;

import macromedia.asc.parser.DocCommentNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class DocCommentNodeVisitor extends MetaDataNodeVisitor<DocCommentNode> {
    @Override
    protected List<Node> getChildren(DocCommentNode node) {
        return Collections.<Node>singletonList(node.metaData);
    }

    @Override
    protected List<Object> getLeaves(DocCommentNode node) {
        return Collections.<Object>singletonList(node.is_default);
    }
}
