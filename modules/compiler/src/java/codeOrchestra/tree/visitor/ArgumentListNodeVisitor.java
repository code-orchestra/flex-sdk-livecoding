package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ArgumentListNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ArgumentListNodeVisitor extends NodeVisitor<ArgumentListNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final ArgumentListNode node) {
        return new LinkedHashMap<Node, String>() {{
            for (Node item : node.items) {
                put(item, "items");
            }
        }};
    }

    @Override
    protected List<Object> getLeaves(final ArgumentListNode node) {
        return new ArrayList<Object>() {{
            add(node.decl_styles);
            add(node.is_bracket_selector);
        }};
    }
}
