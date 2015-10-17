package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.UseDirectiveNode;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class UseDirectiveNodeVisitor extends DefinitionNodeVisitor<UseDirectiveNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final UseDirectiveNode node) {
        return new LinkedHashMap<Node, String>() {{
            putAll(UseDirectiveNodeVisitor.super.getChildren(node));
            put(node.expr, "expr");
        }};
    }

    @Override
    protected List<Object> getLeaves(UseDirectiveNode node) {
        return super.getLeaves(node);
    }
}
