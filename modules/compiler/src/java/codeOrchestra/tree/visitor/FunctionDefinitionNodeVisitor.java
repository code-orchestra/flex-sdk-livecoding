package codeOrchestra.tree.visitor;

import macromedia.asc.parser.FunctionDefinitionNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class FunctionDefinitionNodeVisitor<N extends FunctionDefinitionNode> extends DefinitionNodeVisitor<N> {
    @Override
    protected List<Node> getChildren(final N node) {
        return new ArrayList<Node>() {{
            addAll(FunctionDefinitionNodeVisitor.super.getChildren(node));
            add(node.name);
            add(node.fexpr);
            add(node.init);
        }};
    }

    @Override
    protected List<Object> getLeaves(final N node) {
        return new ArrayList<Object>() {{
            addAll(FunctionDefinitionNodeVisitor.super.getLeaves(node));
            add(node.fun);
        }};
    }
}
