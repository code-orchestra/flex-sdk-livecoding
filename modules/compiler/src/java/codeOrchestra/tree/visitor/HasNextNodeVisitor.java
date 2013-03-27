package codeOrchestra.tree.visitor;

import macromedia.asc.parser.HasNextNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class HasNextNodeVisitor extends NodeVisitor<HasNextNode> {
    @Override
    protected List<Node> getChildren(final HasNextNode node) {
        return new ArrayList<Node>() {{
            add(node.indexRegister);
            add(node.objectRegister);
        }};
    }

    @Override
    protected List<Object> getLeaves(HasNextNode node) {
        return Collections.emptyList();
    }
}
