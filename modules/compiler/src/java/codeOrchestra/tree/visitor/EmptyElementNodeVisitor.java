package codeOrchestra.tree.visitor;

import macromedia.asc.parser.EmptyElementNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class EmptyElementNodeVisitor extends NodeVisitor<EmptyElementNode> {
    @Override
    protected List<Node> getChildren(EmptyElementNode node) {
        return Collections.emptyList();
    }

    @Override
    protected List<Object> getLeaves(EmptyElementNode node) {
        return Collections.emptyList();
    }
}
