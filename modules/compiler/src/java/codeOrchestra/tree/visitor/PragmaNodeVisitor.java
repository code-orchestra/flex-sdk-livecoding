package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.PragmaNode;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class PragmaNodeVisitor extends NodeVisitor<PragmaNode> {
    @Override
    protected List<Node> getChildren(PragmaNode node) {
        return Collections.<Node>singletonList(node.list);
    }

    @Override
    protected List<Object> getLeaves(PragmaNode node) {
        return Collections.emptyList();
    }
}
