package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.SwitchStatementNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class SwitchStatementNodeVisitor extends NodeVisitor<SwitchStatementNode> {
    @Override
    protected List<Node> getChildren(final SwitchStatementNode node) {
        return new ArrayList<Node>() {{
            add(node.expr);
            add(node.statements);
        }};
    }

    @Override
    protected List<Object> getLeaves(SwitchStatementNode node) {
        return Collections.<Object>singletonList(node.loop_index);
    }
}
