package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.TypedIdentifierNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class TypedIdentifierNodeVisitor extends NodeVisitor<TypedIdentifierNode> {
    @Override
    protected List<Node> getChildren(final TypedIdentifierNode node) {
        return new ArrayList<Node>() {{
            add(node.identifier);
            add(node.type);
        }};
    }

    @Override
    protected List<Object> getLeaves(TypedIdentifierNode node) {
        return Collections.<Object>singletonList(node.no_anno);
    }
}
