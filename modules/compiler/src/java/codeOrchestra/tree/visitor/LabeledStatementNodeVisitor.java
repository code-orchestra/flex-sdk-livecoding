package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LabeledStatementNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class LabeledStatementNodeVisitor extends NodeVisitor<LabeledStatementNode> {
    @Override
    protected List<Node> getChildren(final LabeledStatementNode node) {
        return new ArrayList<Node>() {{
            add(node.label);
            add(node.statement);
        }};
    }

    @Override
    protected List<Object> getLeaves(final LabeledStatementNode node) {
        return new ArrayList<Object>() {{
            add(node.loop_index);
            add(node.is_loop_label);
        }};
    }
}
