package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.WithStatementNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class WithStatementNodeVisitor extends NodeVisitor<WithStatementNode> {
    @Override
    protected List<Node> getChildren(final WithStatementNode node) {
        return new ArrayList<Node>() {{
            add(node.expr);
            add(node.statement);
        }};
    }

    @Override
    protected List<Object> getLeaves(WithStatementNode node) {
        return Collections.<Object>singletonList(node.activation);
    }
}
