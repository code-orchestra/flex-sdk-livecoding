package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralXMLNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class LiteralXMLNodeVisitor extends NodeVisitor<LiteralXMLNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final LiteralXMLNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.list, "list");
        }};
    }

    @Override
    protected List<Object> getLeaves(LiteralXMLNode node) {
        return Collections.<Object>singletonList(node.is_xmllist);
    }
}
