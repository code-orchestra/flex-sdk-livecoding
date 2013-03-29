package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ApplyTypeExprNode;
import macromedia.asc.parser.Node;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ApplyTypeExprNodeVisitor extends SelectorNodeVisitor<ApplyTypeExprNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final ApplyTypeExprNode node) {
        return new LinkedHashMap<Node, String>() {{
            putAll(ApplyTypeExprNodeVisitor.super.getChildren(node));
            put(node.typeArgs, "typeArgs");
        }};
    }

    @Override
    protected List<Object> getLeaves(ApplyTypeExprNode node) {
        return super.getLeaves(node);
    }
}
