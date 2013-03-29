package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ContinueStatementNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ContinueStatementNodeVisitor extends NodeVisitor<ContinueStatementNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final ContinueStatementNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.id, "id");
        }};
    }

    @Override
    protected List<Object> getLeaves(ContinueStatementNode node) {
        return Collections.<Object>singletonList(node.loop_index);
    }
}
