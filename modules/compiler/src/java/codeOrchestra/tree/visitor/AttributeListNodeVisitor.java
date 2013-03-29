package codeOrchestra.tree.visitor;

import macromedia.asc.parser.AttributeListNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class AttributeListNodeVisitor extends NodeVisitor<AttributeListNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final AttributeListNode node) {
        return new LinkedHashMap<Node, String>() {{
            for (Node item : node.items) {
                put(item, "items");
            }
        }};
    }

    @Override
    protected List<Object> getLeaves(final AttributeListNode node) {
        return new ArrayList<Object>() {{
            add(node.namespaces);
            add(node.namespace_ids);
            add(node.getUserNamespace());
        }};
    }
}
