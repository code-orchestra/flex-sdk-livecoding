package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LabeledStatementNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class LabeledStatementNodeVisitor extends NodeVisitor<LabeledStatementNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final LabeledStatementNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.label, "label");
            put(node.statement, "statement");
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
