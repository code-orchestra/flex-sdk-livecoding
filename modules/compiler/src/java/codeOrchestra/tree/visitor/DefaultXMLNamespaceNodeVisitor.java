package codeOrchestra.tree.visitor;

import macromedia.asc.parser.DefaultXMLNamespaceNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class DefaultXMLNamespaceNodeVisitor extends NodeVisitor<DefaultXMLNamespaceNode> {
    @Override
    protected List<Node> getChildren(DefaultXMLNamespaceNode node) {
        return Collections.singletonList(node.expr);
    }

    @Override
    protected List<Object> getLeaves(DefaultXMLNamespaceNode node) {
        return Collections.<Object>singletonList(node.ref);
    }
}
