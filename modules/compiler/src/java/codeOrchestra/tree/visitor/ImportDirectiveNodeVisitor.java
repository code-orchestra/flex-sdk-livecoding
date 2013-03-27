package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ImportDirectiveNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ImportDirectiveNodeVisitor extends DefinitionNodeVisitor<ImportDirectiveNode> {
    @Override
    protected List<Node> getChildren(final ImportDirectiveNode node) {
        return new ArrayList<Node>() {{
            addAll(ImportDirectiveNodeVisitor.super.getChildren(node));
            add(node.attrs);
            add(node.name);
        }};
    }

    @Override
    protected List<Object> getLeaves(ImportDirectiveNode node) {
        return super.getLeaves(node);
    }
}
