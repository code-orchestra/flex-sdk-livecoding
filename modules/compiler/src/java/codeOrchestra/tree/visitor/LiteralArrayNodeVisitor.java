package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralArrayNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class LiteralArrayNodeVisitor extends NodeVisitor<LiteralArrayNode> {
    @Override
    protected List<Node> getChildren(LiteralArrayNode node) {
        return Collections.<Node>singletonList(node.elementlist);
    }

    @Override
    protected List<Object> getLeaves(LiteralArrayNode node) {
        return Collections.<Object>singletonList(node.value);
    }
}
