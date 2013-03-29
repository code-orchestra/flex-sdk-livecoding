package codeOrchestra.tree.visitor;

import macromedia.asc.parser.NamespaceDefinitionNode;
import macromedia.asc.parser.Node;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class NamespaceDefinitionNodeVisitor<N extends NamespaceDefinitionNode> extends DefinitionNodeVisitor<N> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final N node) {
        return new LinkedHashMap<Node, String>() {{
            putAll(NamespaceDefinitionNodeVisitor.super.getChildren(node));
            put(node.name, "name");
            put(node.value, "value");
        }};
    }

    @Override
    protected List<Object> getLeaves(N node) {
        return super.getLeaves(node);
    }
}
