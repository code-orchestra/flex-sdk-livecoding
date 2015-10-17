package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.SuperStatementNode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class SuperStatementNodeVisitor extends NodeVisitor<SuperStatementNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final SuperStatementNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.call, "call");
        }};
    }

    @Override
    protected List<Object> getLeaves(SuperStatementNode node) {
        return Collections.<Object>singletonList(node.baseobj);
    }
}
