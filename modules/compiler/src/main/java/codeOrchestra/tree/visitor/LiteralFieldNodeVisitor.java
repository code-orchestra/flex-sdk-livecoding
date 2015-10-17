package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralFieldNode;
import macromedia.asc.parser.Node;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class LiteralFieldNodeVisitor extends NodeVisitor<LiteralFieldNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final LiteralFieldNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.name, "name");
            put(node.value, "value");
        }};
    }

    @Override
    protected List<Object> getLeaves(LiteralFieldNode node) {
        return Collections.<Object>singletonList(node.ref);
    }
}
