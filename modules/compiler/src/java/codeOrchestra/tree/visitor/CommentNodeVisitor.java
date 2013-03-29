package codeOrchestra.tree.visitor;

import macromedia.asc.parser.CommentNode;
import macromedia.asc.parser.Node;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class CommentNodeVisitor extends NodeVisitor<CommentNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(CommentNode node) {
        return emptyMap;
    }

    @Override
    protected List<Object> getLeaves(final CommentNode node) {
        return new ArrayList<Object>() {{
            add(node.getType());
            add(node.toString());
        }};
    }
}
