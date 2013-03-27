package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.RegisterNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class RegisterNodeVisitor extends NodeVisitor<RegisterNode> {
    @Override
    protected List<Node> getChildren(RegisterNode node) {
        return Collections.emptyList();
    }

    @Override
    protected List<Object> getLeaves(final RegisterNode node) {
        return new ArrayList<Object>() {{
            add(node.index);
            add(node.type);
            add(node.void_result);
        }};
    }
}
