package codeOrchestra.tree.visitor;

import macromedia.asc.parser.CommentNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class CommentNodeVisitor extends NodeVisitor<CommentNode> {
    @Override
    protected List<Node> getChildren(CommentNode node) {
        return Collections.emptyList();
    }

    @Override
    protected List<Object> getLeaves(final CommentNode node) {
        return new ArrayList<Object>() {{
            add(node.getType());
            add(node.toString());
        }};
    }
}
