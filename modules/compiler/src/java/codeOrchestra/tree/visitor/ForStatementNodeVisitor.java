package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ForStatementNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ForStatementNodeVisitor extends NodeVisitor<ForStatementNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final ForStatementNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.initialize, "initialize");
            put(node.test, "test");
            put(node.increment, "increment");
            put(node.statement, "statement");
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
