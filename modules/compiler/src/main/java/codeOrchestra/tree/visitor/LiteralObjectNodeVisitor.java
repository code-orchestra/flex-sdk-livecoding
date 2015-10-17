package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralObjectNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class LiteralObjectNodeVisitor extends NodeVisitor<LiteralObjectNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final LiteralObjectNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.fieldlist, "fieldlist");
        }};
    }

    @Override
    protected List<Object> getLeaves(LiteralObjectNode node) {
        return Collections.emptyList();
    }
}
