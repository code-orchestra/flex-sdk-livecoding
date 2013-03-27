package codeOrchestra.tree.visitor;

import macromedia.asc.parser.FunctionNameNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class FunctionNameNodeVisitor extends NodeVisitor<FunctionNameNode> {
    @Override
    protected List<Node> getChildren(FunctionNameNode node) {
        return Collections.<Node>singletonList(node.identifier);
    }

    @Override
    protected List<Object> getLeaves(FunctionNameNode node) {
        return Collections.<Object>singletonList(node.kind);
    }
}
