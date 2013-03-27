package codeOrchestra.tree.visitor;

import macromedia.asc.parser.IncludeDirectiveNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class IncludeDirectiveNodeVisitor extends DefinitionNodeVisitor<IncludeDirectiveNode> {
    @Override
    protected List<Node> getChildren(final IncludeDirectiveNode node) {
        return new ArrayList<Node>() {{
            addAll(IncludeDirectiveNodeVisitor.super.getChildren(node));
            add(node.filespec);
        }};
    }

    @Override
    protected List<Object> getLeaves(IncludeDirectiveNode node) {
        return super.getLeaves(node);
    }
}
