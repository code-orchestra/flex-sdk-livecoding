package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.UsePragmaNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class UsePragmaNodeVisitor<N extends UsePragmaNode> extends NodeVisitor<N> {
    @Override
    protected List<Node> getChildren(final N node) {
        return new ArrayList<Node>() {{
            add(node.identifier);
            add(node.argument);
        }};
    }

    @Override
    protected List<Object> getLeaves(N node) {
        return Collections.emptyList();
    }
}
