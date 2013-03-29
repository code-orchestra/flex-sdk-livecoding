package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.UsePragmaNode;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class UsePragmaNodeVisitor<N extends UsePragmaNode> extends NodeVisitor<N> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final N node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.identifier, "identifier");
            put(node.argument, "argument");
        }};
    }

    @Override
    protected List<Object> getLeaves(N node) {
        return Collections.emptyList();
    }
}
