package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.QualifiedIdentifierNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class QualifiedIdentifierNodeVisitor<N extends QualifiedIdentifierNode> extends IdentifierNodeVisitor<N> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final N node) {
        return new LinkedHashMap<Node, String>() {{
            putAll(QualifiedIdentifierNodeVisitor.super.getChildren(node));
            put(node.qualifier, "qualifier");
        }};
    }

    @Override
    protected List<Object> getLeaves(final N node) {
        return new ArrayList<Object>() {{
            addAll(QualifiedIdentifierNodeVisitor.super.getLeaves(node));
            add(node.is_config_name);
        }};
    }
}
