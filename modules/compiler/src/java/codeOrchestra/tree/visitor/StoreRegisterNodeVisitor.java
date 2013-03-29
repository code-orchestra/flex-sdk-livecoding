package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.StoreRegisterNode;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class StoreRegisterNodeVisitor extends NodeVisitor<StoreRegisterNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final StoreRegisterNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.reg, "reg");
            put(node.expr, "expr");
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
