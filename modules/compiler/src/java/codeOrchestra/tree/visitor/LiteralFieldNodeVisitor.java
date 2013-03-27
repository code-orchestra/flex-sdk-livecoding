package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralFieldNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class LiteralFieldNodeVisitor extends NodeVisitor<LiteralFieldNode> {
    @Override
    protected List<Node> getChildren(final LiteralFieldNode node) {
        return new ArrayList<Node>() {{
            add(node.name);
            add(node.value);
        }};
    }

    @Override
    protected List<Object> getLeaves(LiteralFieldNode node) {
        return Collections.<Object>singletonList(node.ref);
    }
}
