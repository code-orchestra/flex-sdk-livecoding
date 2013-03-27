package codeOrchestra.tree.visitor;

import macromedia.asc.parser.InterfaceDefinitionNode;
import macromedia.asc.parser.Node;

import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class InterfaceDefinitionNodeVisitor extends ClassDefinitionNodeVisitor<InterfaceDefinitionNode> {
    @Override
    protected List<Node> getChildren(InterfaceDefinitionNode node) {
        return super.getChildren(node);
    }

    @Override
    protected List<Object> getLeaves(InterfaceDefinitionNode node) {
        return super.getLeaves(node);
    }
}
