package codeOrchestra.tree.visitor;

import macromedia.asc.parser.IncludeDirectiveNode;
import macromedia.asc.parser.Node;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class IncludeDirectiveNodeVisitor extends DefinitionNodeVisitor<IncludeDirectiveNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final IncludeDirectiveNode node) {
        return new LinkedHashMap<Node, String>() {{
            putAll(IncludeDirectiveNodeVisitor.super.getChildren(node));
            put(node.filespec, "filespec");
        }};
    }

    @Override
    protected List<Object> getLeaves(IncludeDirectiveNode node) {
        return super.getLeaves(node);
    }
}
