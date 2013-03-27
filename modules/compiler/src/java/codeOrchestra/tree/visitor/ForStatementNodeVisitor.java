package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ForStatementNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ForStatementNodeVisitor extends NodeVisitor<ForStatementNode> {
    @Override
    protected List<Node> getChildren(final ForStatementNode node) {
        return new ArrayList<Node>() {{
            add(node.initialize);
            add(node.test);
            add(node.increment);
            add(node.statement);
        }};
    }

    @Override
    protected List<Object> getLeaves(final ForStatementNode node) {
        return new ArrayList<Object>() {{
            add(node.is_forin);
            add(node.loop_index);
        }};
    }
}
