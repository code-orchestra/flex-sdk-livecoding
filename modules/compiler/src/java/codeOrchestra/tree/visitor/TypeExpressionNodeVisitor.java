package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.TypeExpressionNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class TypeExpressionNodeVisitor extends NodeVisitor<TypeExpressionNode> {
    @Override
    protected List<Node> getChildren(TypeExpressionNode node) {
        return Collections.singletonList(node.expr);
    }

    @Override
    protected List<Object> getLeaves(final TypeExpressionNode node) {
        return new ArrayList<Object>() {{
            add(node.nullable_annotation);
            add(node.is_nullable);
        }};
    }
}
