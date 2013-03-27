package codeOrchestra.tree.visitor;

import macromedia.asc.parser.FunctionCommonNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class FunctionCommonNodeVisitor extends NodeVisitor<FunctionCommonNode> {
    @Override
    protected List<Node> getChildren(final FunctionCommonNode node) {
        return new ArrayList<Node>() {{
            add(node.identifier);
            add(node.signature);
            add(node.body);
        }};
    }

    @Override
    protected List<Object> getLeaves(FunctionCommonNode node) {
        return Collections.<Object>singletonList(node.kind);
    }
}
