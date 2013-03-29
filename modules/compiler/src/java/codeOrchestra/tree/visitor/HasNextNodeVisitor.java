package codeOrchestra.tree.visitor;

import macromedia.asc.parser.HasNextNode;
import macromedia.asc.parser.Node;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class HasNextNodeVisitor extends NodeVisitor<HasNextNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final HasNextNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.indexRegister, "indexRegister");
            put(node.objectRegister, "objectRegister");
        }};
    }

    @Override
    protected List<Object> getLeaves(HasNextNode node) {
        return Collections.emptyList();
    }
}
