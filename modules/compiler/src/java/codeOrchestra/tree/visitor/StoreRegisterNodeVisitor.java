package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.StoreRegisterNode;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class StoreRegisterNodeVisitor extends NodeVisitor<StoreRegisterNode> {
    @Override
    protected List<Node> getChildren(final StoreRegisterNode node) {
        return new ArrayList<Node>() {{
            add(node.reg);
            add(node.expr);
        }};
    }

    @Override
    protected List<Object> getLeaves(final StoreRegisterNode node) {
        return new ArrayList<Object>() {{
            add(node.type);
            add(node.void_result);
        }};
    }
}
