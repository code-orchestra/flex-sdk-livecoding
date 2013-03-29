package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralNumberNode;
import macromedia.asc.parser.Node;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class LiteralNumberNodeVisitor extends NodeVisitor<LiteralNumberNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(LiteralNumberNode node) {
        return emptyMap;
    }

    @Override
    protected List<Object> getLeaves(final LiteralNumberNode node) {
        return new ArrayList<Object>() {{
            add(node.type);
            add(node.value);
            add(node.numericValue);
            add(node.numberUsage);
        }};
    }
}
