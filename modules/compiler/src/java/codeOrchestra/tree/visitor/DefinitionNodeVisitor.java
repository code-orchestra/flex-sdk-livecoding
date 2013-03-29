package codeOrchestra.tree.visitor;

import macromedia.asc.parser.DefinitionNode;
import macromedia.asc.parser.Node;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public abstract class DefinitionNodeVisitor<N extends DefinitionNode> extends NodeVisitor<N> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final N node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.attrs, "attrs");
            put(node.metaData, "metaData");
        }};
    }

    @Override
    protected List<Object> getLeaves(N node) {
        return Collections.<Object>singletonList(node.skip());
    }
}
