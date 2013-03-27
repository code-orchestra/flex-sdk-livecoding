package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.QualifiedIdentifierNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class QualifiedIdentifierNodeVisitor<N extends QualifiedIdentifierNode> extends IdentifierNodeVisitor<N> {
    @Override
    protected List<Node> getChildren(final N node) {
        return new ArrayList<Node>() {{
            addAll(QualifiedIdentifierNodeVisitor.super.getChildren(node));
            add(node.qualifier);
        }};
    }

    @Override
    protected List<Object> getLeaves(final N node) {
        return new ArrayList<Object>() {{
            addAll(QualifiedIdentifierNodeVisitor.super.getLeaves(node));
            add(node.is_config_name);
        }};
    }
}
