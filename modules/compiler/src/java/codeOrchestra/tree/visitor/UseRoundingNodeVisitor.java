package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.UseRoundingNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class UseRoundingNodeVisitor extends UsePragmaNodeVisitor<UseRoundingNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(UseRoundingNode node) {
        return super.getChildren(node);
    }

    @Override
    protected List<Object> getLeaves(final UseRoundingNode node) {
        return new ArrayList<Object>() {{
            addAll(UseRoundingNodeVisitor.super.getLeaves(node));
            add(node.mode);
        }};
    }
}
