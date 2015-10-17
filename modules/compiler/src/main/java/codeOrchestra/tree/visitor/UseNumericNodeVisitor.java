package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.UseNumericNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class UseNumericNodeVisitor extends UsePragmaNodeVisitor<UseNumericNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(UseNumericNode node) {
        return super.getChildren(node);
    }

    @Override
    protected List<Object> getLeaves(final UseNumericNode node) {
        return new ArrayList<Object>() {{
            addAll(UseNumericNodeVisitor.super.getLeaves(node));
            add(node.numeric_mode);
        }};
    }
}
