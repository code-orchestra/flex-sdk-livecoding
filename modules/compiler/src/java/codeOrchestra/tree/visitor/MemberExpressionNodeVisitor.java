package codeOrchestra.tree.visitor;

import macromedia.asc.parser.MemberExpressionNode;
import macromedia.asc.parser.Node;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class MemberExpressionNodeVisitor extends NodeVisitor<MemberExpressionNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final MemberExpressionNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.base, "base");
            put(node.selector, "selector");
        }};
    }

    @Override
    protected List<Object> getLeaves(MemberExpressionNode node) {
        return Collections.<Object>singletonList(node.ref);
    }
}
