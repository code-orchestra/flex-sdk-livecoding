package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.UntypedVariableBindingNode;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class UntypedVariableBindingNodeVisitor extends NodeVisitor<UntypedVariableBindingNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final UntypedVariableBindingNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.identifier, "identifier");
            put(node.initializer, "initializer");
        }};
    }

    @Override
    protected List<Object> getLeaves(UntypedVariableBindingNode node) {
        return Collections.<Object>singletonList(node.ref);
    }
}
