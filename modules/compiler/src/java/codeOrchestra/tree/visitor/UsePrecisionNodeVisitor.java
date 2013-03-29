package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.UsePrecisionNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class UsePrecisionNodeVisitor extends UsePragmaNodeVisitor<UsePrecisionNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(UsePrecisionNode node) {
        return super.getChildren(node);
    }

    @Override
    protected List<Object> getLeaves(final UsePrecisionNode node) {
        return new ArrayList<Object>() {{
            addAll(UsePrecisionNodeVisitor.super.getLeaves(node));
            add(node.precision);
        }};
    }
}
