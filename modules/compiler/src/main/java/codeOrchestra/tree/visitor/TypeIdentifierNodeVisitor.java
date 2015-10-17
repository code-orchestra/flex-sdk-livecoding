package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.TypeIdentifierNode;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class TypeIdentifierNodeVisitor extends IdentifierNodeVisitor<TypeIdentifierNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final TypeIdentifierNode node) {
        return new LinkedHashMap<Node, String>() {{
            putAll(TypeIdentifierNodeVisitor.super.getChildren(node));
            put(node.base, "base");
            put(node.typeArgs, "typeArgs");
        }};
    }

    @Override
    protected List<Object> getLeaves(TypeIdentifierNode node) {
        return super.getLeaves(node);
    }
}
