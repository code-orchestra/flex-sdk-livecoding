package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.TryStatementNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class TryStatementNodeVisitor extends NodeVisitor<TryStatementNode> {
    @Override
    protected List<Node> getChildren(final TryStatementNode node) {
        return new ArrayList<Node>() {{
            add(node.tryblock);
            add(node.catchlist);
            add(node.finallyblock);
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
