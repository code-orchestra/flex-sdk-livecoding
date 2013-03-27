package codeOrchestra.tree.visitor;

import macromedia.asc.parser.MemberExpressionNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class MemberExpressionNodeVisitor extends NodeVisitor<MemberExpressionNode> {
    @Override
    protected List<Node> getChildren(final MemberExpressionNode node) {
        return new ArrayList<Node>() {{
            add(node.base);
            add(node.selector);
        }};
    }

    @Override
    protected List<Object> getLeaves(MemberExpressionNode node) {
        return Collections.<Object>singletonList(node.ref);
    }
}
