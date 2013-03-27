package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralObjectNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class LiteralObjectNodeVisitor extends NodeVisitor<LiteralObjectNode> {
    @Override
    protected List<Node> getChildren(LiteralObjectNode node) {
        return Collections.<Node>singletonList(node.fieldlist);
    }

    @Override
    protected List<Object> getLeaves(LiteralObjectNode node) {
        return Collections.emptyList();
    }
}
