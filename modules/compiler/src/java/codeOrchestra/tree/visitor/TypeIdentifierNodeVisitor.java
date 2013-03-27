package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.TypeIdentifierNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class TypeIdentifierNodeVisitor extends IdentifierNodeVisitor<TypeIdentifierNode> {
    @Override
    protected List<Node> getChildren(final TypeIdentifierNode node) {
        return new ArrayList<Node>() {{
            addAll(TypeIdentifierNodeVisitor.super.getChildren(node));
            add(node.base);
            add(node.typeArgs);
        }};
    }

    @Override
    protected List<Object> getLeaves(TypeIdentifierNode node) {
        return super.getLeaves(node);
    }
}
