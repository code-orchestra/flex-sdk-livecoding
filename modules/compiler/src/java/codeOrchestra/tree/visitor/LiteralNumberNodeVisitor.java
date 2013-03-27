package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralNumberNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class LiteralNumberNodeVisitor extends NodeVisitor<LiteralNumberNode> {
    @Override
    protected List<Node> getChildren(LiteralNumberNode node) {
        return Collections.emptyList();
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
