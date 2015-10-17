package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.PragmaExpressionNode;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class PragmaExpressionNodeVisitor extends NodeVisitor<PragmaExpressionNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final PragmaExpressionNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.identifier, "identifier");
            put(node.arg, "arg");
        }};
    }

    @Override
    protected List<Object> getLeaves(PragmaExpressionNode node) {
        return Collections.emptyList();
    }
}
