package codeOrchestra.tree.visitor;

import macromedia.asc.parser.MetaDataNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class MetaDataNodeVisitor<N extends MetaDataNode> extends NodeVisitor<N> {
    @Override
    protected List<Node> getChildren(N node) {
        return Collections.<Node>singletonList(node.data);
    }

    @Override
    protected List<Object> getLeaves(N node) {
        return Collections.<Object>singletonList(node.getMetadata());
    }
}
