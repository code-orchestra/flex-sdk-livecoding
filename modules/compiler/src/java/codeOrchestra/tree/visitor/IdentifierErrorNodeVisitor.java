package codeOrchestra.tree.visitor;

import macromedia.asc.parser.IdentifierErrorNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class IdentifierErrorNodeVisitor extends IdentifierNodeVisitor<IdentifierErrorNode> {
    @Override
    protected List<Node> getChildren(IdentifierErrorNode node) {
        return super.getChildren(node);
    }

    @Override
    protected List<Object> getLeaves(final IdentifierErrorNode node) {
        return new ArrayList<Object>() {{
            addAll(IdentifierErrorNodeVisitor.super.getChildren(node));
            add(node.value);
        }};
    }
}
