package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralVectorNode;
import macromedia.asc.parser.Node;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class LiteralVectorNodeVisitor extends NodeVisitor<LiteralVectorNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final LiteralVectorNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.type, "type");
            put(node.elementlist, "elementlist");
        }};
    }

    @Override
    protected List<Object> getLeaves(LiteralVectorNode node) {
        return Collections.emptyList();
    }
}
