package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.UseDirectiveNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class UseDirectiveNodeVisitor extends DefinitionNodeVisitor<UseDirectiveNode> {
    @Override
    protected List<Node> getChildren(final UseDirectiveNode node) {
        return new ArrayList<Node>() {{
            addAll(UseDirectiveNodeVisitor.super.getChildren(node));
            add(node.expr);
        }};
    }

    @Override
    protected List<Object> getLeaves(UseDirectiveNode node) {
        return super.getLeaves(node);
    }
}
