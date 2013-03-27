package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.PragmaExpressionNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class PragmaExpressionNodeVisitor extends NodeVisitor<PragmaExpressionNode> {
    @Override
    protected List<Node> getChildren(final PragmaExpressionNode node) {
        return new ArrayList<Node>() {{
            add(node.identifier);
            add(node.arg);
        }};
    }

    @Override
    protected List<Object> getLeaves(PragmaExpressionNode node) {
        return Collections.emptyList();
    }
}
