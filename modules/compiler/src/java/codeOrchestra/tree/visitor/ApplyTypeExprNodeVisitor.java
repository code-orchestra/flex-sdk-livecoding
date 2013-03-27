package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ApplyTypeExprNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ApplyTypeExprNodeVisitor extends SelectorNodeVisitor<ApplyTypeExprNode> {
    @Override
    protected List<Node> getChildren(final ApplyTypeExprNode node) {
        return new ArrayList<Node>() {{
            addAll(ApplyTypeExprNodeVisitor.super.getChildren(node));
            add(node.typeArgs);
        }};
    }

    @Override
    protected List<Object> getLeaves(ApplyTypeExprNode node) {
        return super.getLeaves(node);
    }
}
