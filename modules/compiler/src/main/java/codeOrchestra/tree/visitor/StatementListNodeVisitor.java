package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.StatementListNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class StatementListNodeVisitor extends NodeVisitor<StatementListNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final StatementListNode node) {
        return new LinkedHashMap<Node, String>() {{
            for (Node item : node.items) {
                put(item, "items");
            }
            put(node.config_attrs, "config_attrs");
        }};
    }

    @Override
    protected List<Object> getLeaves(final StatementListNode node) {
        return new ArrayList<Object>() {{
            add(node.dominates_program_endpoint);
            add(node.was_empty);
            add(node.is_loop);
            add(node.is_block);
            add(node.has_pragma);
            add(node.numberUsage);
            add(node.default_namespace);
        }};
    }
}
