package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralNullNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class LiteralNullNodeVisitor extends NodeVisitor<LiteralNullNode> {
    @Override
    protected List<Node> getChildren(LiteralNullNode node) {
        return Collections.emptyList();
    }

    @Override
    protected List<Object> getLeaves(LiteralNullNode node) {
        return Collections.emptyList();
    }
}
