package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.SwitchStatementNode;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class SwitchStatementNodeVisitor extends NodeVisitor<SwitchStatementNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final SwitchStatementNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.expr, "expr");
            put(node.statements, "statements");
        }};
    }

    @Override
    protected List<Object> getLeaves(SwitchStatementNode node) {
        return Collections.<Object>singletonList(node.loop_index);
    }
}
