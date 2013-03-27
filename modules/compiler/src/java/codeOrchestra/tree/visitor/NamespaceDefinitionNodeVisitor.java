package codeOrchestra.tree.visitor;

import macromedia.asc.parser.NamespaceDefinitionNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class NamespaceDefinitionNodeVisitor<N extends NamespaceDefinitionNode> extends DefinitionNodeVisitor<N> {
    @Override
    protected List<Node> getChildren(final N node) {
        return new ArrayList<Node>() {{
            addAll(NamespaceDefinitionNodeVisitor.super.getChildren(node));
            add(node.name);
            add(node.value);
        }};
    }

    @Override
    protected List<Object> getLeaves(N node) {
        return super.getLeaves(node);
    }
}
