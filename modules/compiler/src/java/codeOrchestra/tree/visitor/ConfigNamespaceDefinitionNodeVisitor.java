package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ConfigNamespaceDefinitionNode;
import macromedia.asc.parser.Node;

import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ConfigNamespaceDefinitionNodeVisitor extends NamespaceDefinitionNodeVisitor<ConfigNamespaceDefinitionNode> {
    @Override
    protected List<Node> getChildren(ConfigNamespaceDefinitionNode node) {
        return super.getChildren(node);
    }

    @Override
    protected List<Object> getLeaves(ConfigNamespaceDefinitionNode node) {
        return super.getLeaves(node);
    }
}
