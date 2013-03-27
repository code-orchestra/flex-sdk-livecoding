package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralStringNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class LiteralStringNodeVisitor extends NodeVisitor<LiteralStringNode> {
    @Override
    protected List<Node> getChildren(LiteralStringNode node) {
        return Collections.emptyList();
    }

    @Override
    protected List<Object> getLeaves(LiteralStringNode node) {
        return Collections.<Object>singletonList(node.value);
    }
}
