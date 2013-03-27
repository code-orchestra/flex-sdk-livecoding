package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.ParameterNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ParameterNodeVisitor<N extends ParameterNode> extends NodeVisitor<N> {
    @Override
    protected List<Node> getChildren(final N node) {
        return new ArrayList<Node>() {{
            add(node.identifier);
            add(node.type);
            add(node.init);
            add(node.attrs);
        }};
    }

    @Override
    protected List<Object> getLeaves(final N node) {
        return new ArrayList<Object>() {{
            add(node.kind);
            add(node.ref);
            add(node.typeref);
            add(node.no_anno);
        }};
    }
}
