package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.SelectorNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public abstract class SelectorNodeVisitor<N extends SelectorNode> extends NodeVisitor<N> {
    @Override
    protected List<Node> getChildren(N node) {
        return Collections.singletonList(node.expr);
    }

    @Override
    protected List<Object> getLeaves(final N node) {
        return new ArrayList<Object>() {{
            add(node.base);
            add(node.ref);
            add(node.is_package);
            add(node.getFlags());
        }};
    }
}
