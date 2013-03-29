package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.ParameterListNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ParameterListNodeVisitor extends NodeVisitor<ParameterListNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final ParameterListNode node) {
        return new LinkedHashMap<Node, String>() {{
            for (Node item : node.items) {
                put(item, "items");
            }
        }};
    }

    @Override
    protected List<Object> getLeaves(final ParameterListNode node) {
        return new ArrayList<Object>() {{
            add(node.types);
            add(node.decl_styles);
        }};
    }
}
