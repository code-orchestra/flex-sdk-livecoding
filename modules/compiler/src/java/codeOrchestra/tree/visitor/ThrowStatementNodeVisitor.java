package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.ThrowStatementNode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ThrowStatementNodeVisitor extends NodeVisitor<ThrowStatementNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final ThrowStatementNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.expr, "expr");
        }};
    }

    @Override
    protected List<Object> getLeaves(ThrowStatementNode node) {
        return Collections.<Object>singletonList(node.finallyInserted);
    }
}
