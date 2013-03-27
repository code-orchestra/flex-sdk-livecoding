package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ArgumentListNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ArgumentListNodeVisitor extends NodeVisitor<ArgumentListNode> {
    @Override
    protected List<Node> getChildren(ArgumentListNode node) {
        return node.items;
    }

    @Override
    protected List<Object> getLeaves(final ArgumentListNode node) {
        return new ArrayList<Object>() {{
            add(node.decl_styles);
            add(node.is_bracket_selector);
        }};
    }
}
