package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.VariableBindingNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class VariableBindingNodeVisitor extends NodeVisitor<VariableBindingNode> {
    @Override
    protected List<Node> getChildren(final VariableBindingNode node) {
        return new ArrayList<Node>() {{
            add(node.variable);
            add(node.initializer);
            add(node.attrs);
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
