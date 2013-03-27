package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ClassDefinitionNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ClassDefinitionNodeVisitor<N extends ClassDefinitionNode> extends DefinitionNodeVisitor<N> {
    @Override
    protected List<Node> getChildren(final N node) {
        return new ArrayList<Node>() {{
            addAll(ClassDefinitionNodeVisitor.super.getChildren(node));
            add(node.name);
            add(node.statements);
            add(node.baseclass);
            add(node.interfaces);
        }};
    }

    @Override
    protected List<Object> getLeaves(N node) {
        return Collections.emptyList();
    }
}
