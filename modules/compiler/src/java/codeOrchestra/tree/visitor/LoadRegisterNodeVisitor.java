package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LoadRegisterNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class LoadRegisterNodeVisitor extends NodeVisitor<LoadRegisterNode> {
    @Override
    protected List<Node> getChildren(LoadRegisterNode node) {
        return Collections.<Node>singletonList(node.reg);
    }

    @Override
    protected List<Object> getLeaves(LoadRegisterNode node) {
        return Collections.<Object>singletonList(node.type);
    }
}
