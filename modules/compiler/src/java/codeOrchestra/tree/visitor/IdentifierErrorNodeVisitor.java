package codeOrchestra.tree.visitor;

import macromedia.asc.parser.IdentifierErrorNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class IdentifierErrorNodeVisitor extends IdentifierNodeVisitor<IdentifierErrorNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(IdentifierErrorNode node) {
        return super.getChildren(node);
    }

    @Override
    protected List<Object> getLeaves(final IdentifierErrorNode node) {
        return new ArrayList<Object>() {{
            addAll(IdentifierErrorNodeVisitor.super.getLeaves(node));
            add(node.value);
        }};
    }
}
