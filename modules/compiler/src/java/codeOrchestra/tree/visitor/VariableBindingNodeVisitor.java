package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.VariableBindingNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class VariableBindingNodeVisitor extends NodeVisitor<VariableBindingNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final VariableBindingNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.variable, "variable");
            put(node.initializer, "initializer");
            put(node.attrs, "attrs");
        }};
    }

    @Override
    protected List<Object> getLeaves(final VariableBindingNode node) {
        return new ArrayList<Object>() {{
            add(node.ref);
            add(node.typeref);
            add(node.debug_name);
            add(node.kind);
        }};
    }
}
