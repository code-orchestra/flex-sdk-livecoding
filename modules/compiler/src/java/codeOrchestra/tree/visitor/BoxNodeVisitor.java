package codeOrchestra.tree.visitor;

import macromedia.asc.parser.BoxNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class BoxNodeVisitor extends NodeVisitor<BoxNode> {
    @Override
    protected List<Node> getChildren(BoxNode node) {
        return Collections.singletonList(node.expr);
    }

    @Override
    protected List<Object> getLeaves(final BoxNode node) {
        return new ArrayList<Object>() {{
            add(node.actual);
            add(node.void_result);
        }};
    }
}
