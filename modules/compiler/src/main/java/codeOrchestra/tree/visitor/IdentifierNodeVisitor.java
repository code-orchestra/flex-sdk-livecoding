package codeOrchestra.tree.visitor;

import macromedia.asc.parser.IdentifierNode;
import macromedia.asc.parser.Node;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class IdentifierNodeVisitor<N extends IdentifierNode> extends NodeVisitor<N> {
    @Override
    public LinkedHashMap<Node, String> getChildren(N node) {
        return emptyMap;
    }

    @Override
    protected List<Object> getLeaves(final N node) {
        return new ArrayList<Object>() {{
            add(node.name);
            add(node.ref);
        }};
    }
}
