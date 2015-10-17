package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.TryStatementNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class TryStatementNodeVisitor extends NodeVisitor<TryStatementNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final TryStatementNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.tryblock, "tryblock");
            put(node.catchlist, "catchlist");
            put(node.finallyblock, "finallyblock");
        }};
    }

    @Override
    protected List<Object> getLeaves(final TryStatementNode node) {
        return new ArrayList<Object>() {{
            add(node.finallyInserted);
            add(node.loop_index);
        }};
    }
}
