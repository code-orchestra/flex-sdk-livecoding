package codeOrchestra.tree.visitor;

import macromedia.asc.parser.CaseLabelNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class CaseLabelNodeVisitor extends NodeVisitor<CaseLabelNode> {
    @Override
    protected List<Node> getChildren(CaseLabelNode node) {
        return Collections.singletonList(node.label);
    }

    @Override
    protected List<Object> getLeaves(CaseLabelNode node) {
        return Collections.emptyList();
    }
}
