package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.TypeExpressionNode;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class TypeExpressionNodeVisitor extends NodeVisitor<TypeExpressionNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final TypeExpressionNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.expr, "expr");
        }};
    }

    @Override
    protected List<Object> getLeaves(final TypeExpressionNode node) {
        return new ArrayList<Object>() {{
            add(node.nullable_annotation);
            add(node.is_nullable);
        }};
    }
}
