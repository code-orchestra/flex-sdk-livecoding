package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.ToObjectNode;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ToObjectNodeVisitor extends NodeVisitor<ToObjectNode> {
    @Override
    protected List<Node> getChildren(ToObjectNode node) {
        return Collections.singletonList(node.expr);
    }

    @Override
    protected List<Object> getLeaves(ToObjectNode node) {
        return Collections.emptyList();
    }
}
