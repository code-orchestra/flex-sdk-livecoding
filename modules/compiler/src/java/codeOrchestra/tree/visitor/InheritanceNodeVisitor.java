package codeOrchestra.tree.visitor;

import macromedia.asc.parser.InheritanceNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class InheritanceNodeVisitor extends NodeVisitor<InheritanceNode> {
    @Override
    protected List<Node> getChildren(final InheritanceNode node) {
        return new ArrayList<Node>() {{
            add(node.baseclass);
            add(node.interfaces);
        }};
    }

    @Override
    protected List<Object> getLeaves(InheritanceNode node) {
        return Collections.emptyList();
    }
}
