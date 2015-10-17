package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.RegisterNode;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class RegisterNodeVisitor extends NodeVisitor<RegisterNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(RegisterNode node) {
        return emptyMap;
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
