package codeOrchestra.tree.visitor;

import macromedia.asc.parser.DefaultXMLNamespaceNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class DefaultXMLNamespaceNodeVisitor extends NodeVisitor<DefaultXMLNamespaceNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final DefaultXMLNamespaceNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.expr, "expr");
        }};
    }

    @Override
    protected List<Object> getLeaves(DefaultXMLNamespaceNode node) {
        return Collections.<Object>singletonList(node.ref);
    }
}
