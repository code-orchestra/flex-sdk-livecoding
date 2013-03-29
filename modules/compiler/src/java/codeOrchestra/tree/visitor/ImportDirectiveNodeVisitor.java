package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ImportDirectiveNode;
import macromedia.asc.parser.Node;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ImportDirectiveNodeVisitor extends DefinitionNodeVisitor<ImportDirectiveNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final ImportDirectiveNode node) {
        return new LinkedHashMap<Node, String>() {{
            putAll(ImportDirectiveNodeVisitor.super.getChildren(node));
            put(node.attrs, "attrs");
            put(node.name, "name");
        }};
    }

    @Override
    protected List<Object> getLeaves(ImportDirectiveNode node) {
        return super.getLeaves(node);
    }
}
