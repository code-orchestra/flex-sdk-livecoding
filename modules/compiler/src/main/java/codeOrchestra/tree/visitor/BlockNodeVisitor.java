package codeOrchestra.tree.visitor;

import macromedia.asc.parser.BlockNode;
import macromedia.asc.parser.Node;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class BlockNodeVisitor extends NodeVisitor<BlockNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final BlockNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.attributes, "attributes");
            put(node.statements, "statements");
        }};
    }

    @Override
    protected List<Object> getLeaves(BlockNode node) {
        return Collections.emptyList();
    }
}
