package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralBooleanNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class LiteralBooleanNodeVisitor extends NodeVisitor<LiteralBooleanNode> {
    @Override
    protected List<Node> getChildren(LiteralBooleanNode node) {
        return Collections.emptyList();
    }

    @Override
    protected List<Object> getLeaves(LiteralBooleanNode node) {
        return Collections.<Object>singletonList(node.value);
    }
}
