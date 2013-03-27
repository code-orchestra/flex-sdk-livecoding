package codeOrchestra.tree.visitor;

import macromedia.asc.parser.BlockNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class BlockNodeVisitor extends NodeVisitor<BlockNode> {
    @Override
    protected List<Node> getChildren(final BlockNode node) {
        return new ArrayList<Node>() {{
            add(node.attributes);
            add(node.statements);
        }};
    }

    @Override
    protected List<Object> getLeaves(BlockNode node) {
        return Collections.emptyList();
    }
}
