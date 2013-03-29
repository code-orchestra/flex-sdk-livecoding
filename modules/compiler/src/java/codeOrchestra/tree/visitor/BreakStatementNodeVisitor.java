package codeOrchestra.tree.visitor;

import macromedia.asc.parser.BreakStatementNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class BreakStatementNodeVisitor extends NodeVisitor<BreakStatementNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final BreakStatementNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.id, "id");
        }};
    }

    @Override
    protected List<Object> getLeaves(BreakStatementNode node) {
        return Collections.<Object>singletonList(node.loop_index);
    }
}
