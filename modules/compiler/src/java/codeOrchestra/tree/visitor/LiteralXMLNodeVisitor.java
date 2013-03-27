package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralXMLNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class LiteralXMLNodeVisitor extends NodeVisitor<LiteralXMLNode> {
    @Override
    protected List<Node> getChildren(LiteralXMLNode node) {
        return Collections.<Node>singletonList(node.list);
    }

    @Override
    protected List<Object> getLeaves(LiteralXMLNode node) {
        return Collections.<Object>singletonList(node.is_xmllist);
    }
}
