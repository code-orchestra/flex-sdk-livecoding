package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.UntypedVariableBindingNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class UntypedVariableBindingNodeVisitor extends NodeVisitor<UntypedVariableBindingNode> {
    @Override
    protected List<Node> getChildren(final UntypedVariableBindingNode node) {
        return new ArrayList<Node>() {{
            add(node.identifier);
            add(node.initializer);
        }};
    }

    @Override
    protected List<Object> getLeaves(UntypedVariableBindingNode node) {
        return Collections.<Object>singletonList(node.ref);
    }
}
