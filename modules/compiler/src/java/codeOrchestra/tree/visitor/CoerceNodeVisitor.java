package codeOrchestra.tree.visitor;

import macromedia.asc.parser.CoerceNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class CoerceNodeVisitor extends NodeVisitor<CoerceNode> {
    @Override
    protected List<Node> getChildren(CoerceNode node) {
        return Collections.singletonList(node.expr);
    }

    @Override
    protected List<Object> getLeaves(final CoerceNode node) {
        return new ArrayList<Object>() {{
            add(node.actual);
            add(node.expected);
            add(node.void_result);
            add(node.is_explicit);
        }};
    }
}
