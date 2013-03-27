package codeOrchestra.tree.visitor;

import macromedia.asc.parser.IdentifierNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class IdentifierNodeVisitor<N extends IdentifierNode> extends NodeVisitor<N> {
    @Override
    protected List<Node> getChildren(N node) {
        return Collections.emptyList();
    }

    @Override
    protected List<Object> getLeaves(final N node) {
        return new ArrayList<Object>() {{
            add(node.name);
            add(node.ref);
        }};
    }
}
