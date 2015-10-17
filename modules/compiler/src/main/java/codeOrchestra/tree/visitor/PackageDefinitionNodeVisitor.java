package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.PackageDefinitionNode;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class PackageDefinitionNodeVisitor extends DefinitionNodeVisitor<PackageDefinitionNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final PackageDefinitionNode node) {
        return new LinkedHashMap<Node, String>() {{
            putAll(PackageDefinitionNodeVisitor.super.getChildren(node));
            put(node.name, "name");
            put(node.statements, "statements");
        }};
    }

    @Override
    protected List<Object> getLeaves(PackageDefinitionNode node) {
        return super.getLeaves(node);
    }
}
