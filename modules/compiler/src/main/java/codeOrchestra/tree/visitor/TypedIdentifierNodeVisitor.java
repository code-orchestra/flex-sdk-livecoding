package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.TypedIdentifierNode;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class TypedIdentifierNodeVisitor extends NodeVisitor<TypedIdentifierNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final TypedIdentifierNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.identifier, "identifier");
            put(node.type, "type");
        }};
    }

    @Override
    protected List<Object> getLeaves(TypedIdentifierNode node) {
        return Collections.<Object>singletonList(node.no_anno);
    }
}
