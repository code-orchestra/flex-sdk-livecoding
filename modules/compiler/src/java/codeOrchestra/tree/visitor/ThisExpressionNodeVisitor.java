package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.ThisExpressionNode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ThisExpressionNodeVisitor extends NodeVisitor<ThisExpressionNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(ThisExpressionNode node) {
        return emptyMap;
    }

    @Override
    protected List<Object> getLeaves(ThisExpressionNode node) {
        return Collections.emptyList();
    }
}
