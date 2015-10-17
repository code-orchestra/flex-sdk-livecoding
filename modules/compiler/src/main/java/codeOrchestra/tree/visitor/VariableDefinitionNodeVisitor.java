package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.VariableDefinitionNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class VariableDefinitionNodeVisitor<N extends VariableDefinitionNode> extends DefinitionNodeVisitor<N> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final N node) {
        return new LinkedHashMap<Node, String>() {{
            putAll(VariableDefinitionNodeVisitor.super.getChildren(node));
            put(node.list, "list");
        }};
    }

    @Override
    protected List<Object> getLeaves(final N node) {
        return new ArrayList<Object>() {{
            addAll(VariableDefinitionNodeVisitor.super.getLeaves(node));
            add(node.kind);
        }};
    }
}
