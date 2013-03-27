package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.VariableDefinitionNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class VariableDefinitionNodeVisitor<N extends VariableDefinitionNode> extends DefinitionNodeVisitor<N> {
    @Override
    protected List<Node> getChildren(final N node) {
        return new ArrayList<Node>() {{
            addAll(VariableDefinitionNodeVisitor.super.getChildren(node));
            add(node.list);
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
