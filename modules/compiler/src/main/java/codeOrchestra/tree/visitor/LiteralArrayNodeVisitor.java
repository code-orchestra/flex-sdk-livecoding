package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralArrayNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class LiteralArrayNodeVisitor extends NodeVisitor<LiteralArrayNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final LiteralArrayNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.elementlist, "elementlist");
        }};
    }

    @Override
    protected List<Object> getLeaves(LiteralArrayNode node) {
        return Collections.<Object>singletonList(node.value);
    }
}
