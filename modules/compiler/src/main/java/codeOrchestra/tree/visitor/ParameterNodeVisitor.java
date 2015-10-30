package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.ParameterNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ParameterNodeVisitor<N extends ParameterNode> extends NodeVisitor<N> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final N node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.identifier, "identifier");
            put(node.type, "type");
            put(node.init, "init");
            put(node.attrs, "attrs");
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
