package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.PackageDefinitionNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class PackageDefinitionNodeVisitor extends DefinitionNodeVisitor<PackageDefinitionNode> {
    @Override
    protected List<Node> getChildren(final PackageDefinitionNode node) {
        return new ArrayList<Node>() {{
            addAll(PackageDefinitionNodeVisitor.super.getChildren(node));
            add(node.name);
            add(node.statements);
        }};
    }

    @Override
    protected List<Object> getLeaves(PackageDefinitionNode node) {
        return super.getLeaves(node);
    }
}
