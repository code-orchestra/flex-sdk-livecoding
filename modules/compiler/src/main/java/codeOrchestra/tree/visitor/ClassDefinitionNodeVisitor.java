package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ClassDefinitionNode;
import macromedia.asc.parser.Node;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class ClassDefinitionNodeVisitor<N extends ClassDefinitionNode> extends DefinitionNodeVisitor<N> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final N node) {
        return new LinkedHashMap<Node, String>() {{
            putAll(ClassDefinitionNodeVisitor.super.getChildren(node));
            put(node.name, "name");
            put(node.statements, "statements");
            put(node.baseclass, "baseclass");
            put(node.interfaces, "interfaces");
        }};
    }

    @Override
    protected List<Object> getLeaves(N node) {
        return Collections.emptyList();
    }
}
