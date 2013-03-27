package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralVectorNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class LiteralVectorNodeVisitor extends NodeVisitor<LiteralVectorNode> {
    @Override
    protected List<Node> getChildren(final LiteralVectorNode node) {
        return new ArrayList<Node>() {{
            add(node.type);
            add(node.elementlist);
        }};
    }

    @Override
    protected List<Object> getLeaves(LiteralVectorNode node) {
        return Collections.emptyList();
    }
}
