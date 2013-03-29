package codeOrchestra.tree.visitor;

import macromedia.asc.parser.FunctionDefinitionNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class FunctionDefinitionNodeVisitor<N extends FunctionDefinitionNode> extends DefinitionNodeVisitor<N> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final N node) {
        return new LinkedHashMap<Node, String>() {{
            putAll(FunctionDefinitionNodeVisitor.super.getChildren(node));
            put(node.name, "name");
            put(node.fexpr, "fexpr");
            put(node.init, "init");
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
