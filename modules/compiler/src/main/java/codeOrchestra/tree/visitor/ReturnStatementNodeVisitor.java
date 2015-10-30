package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.ReturnStatementNode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ReturnStatementNodeVisitor extends NodeVisitor<ReturnStatementNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final ReturnStatementNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.expr, "expr");
        }};
    }

    @Override
    protected List<Object> getLeaves(ReturnStatementNode node) {
        return Collections.<Object>singletonList(node.finallyInserted);
    }
}
