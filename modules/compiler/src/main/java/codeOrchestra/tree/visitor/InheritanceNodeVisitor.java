package codeOrchestra.tree.visitor;

import macromedia.asc.parser.InheritanceNode;
import macromedia.asc.parser.Node;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class InheritanceNodeVisitor extends NodeVisitor<InheritanceNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final InheritanceNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.baseclass, "baseclass");
            put(node.interfaces, "interfaces");
        }};
    }

    @Override
    protected List<Object> getLeaves(InheritanceNode node) {
        return Collections.emptyList();
    }
}
