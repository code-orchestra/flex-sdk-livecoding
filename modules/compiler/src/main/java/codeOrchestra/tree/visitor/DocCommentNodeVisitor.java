package codeOrchestra.tree.visitor;

import macromedia.asc.parser.DocCommentNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class DocCommentNodeVisitor extends MetaDataNodeVisitor<DocCommentNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final DocCommentNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.metaData, "metaData");
        }};
    }

    @Override
    protected List<Object> getLeaves(DocCommentNode node) {
        return Collections.<Object>singletonList(node.is_default);
    }
}
