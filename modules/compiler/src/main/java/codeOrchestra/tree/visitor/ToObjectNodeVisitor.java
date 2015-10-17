package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.ToObjectNode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ToObjectNodeVisitor extends NodeVisitor<ToObjectNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final ToObjectNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.expr, "expr");
        }};
    }

    @Override
    protected List<Object> getLeaves(ToObjectNode node) {
        return Collections.emptyList();
    }
}
