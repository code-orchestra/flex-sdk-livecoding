package codeOrchestra.tree.visitor;

import macromedia.asc.parser.MetaDataNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class MetaDataNodeVisitor<N extends MetaDataNode> extends NodeVisitor<N> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final N node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.data, "data");
        }};
    }

    @Override
    protected List<Object> getLeaves(N node) {
        return Collections.<Object>singletonList(node.getMetadata());
    }
}
