package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.UsePrecisionNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class UsePrecisionNodeVisitor extends UsePragmaNodeVisitor<UsePrecisionNode> {
    @Override
    protected List<Node> getChildren(UsePrecisionNode node) {
        return super.getChildren(node);
    }

    @Override
    protected List<Object> getLeaves(final UsePrecisionNode node) {
        return new ArrayList<Object>() {{
            addAll(UsePrecisionNodeVisitor.super.getChildren(node));
            add(node.precision);
        }};
    }
}
