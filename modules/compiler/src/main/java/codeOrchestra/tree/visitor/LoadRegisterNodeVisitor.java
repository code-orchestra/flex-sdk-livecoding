package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LoadRegisterNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class LoadRegisterNodeVisitor extends NodeVisitor<LoadRegisterNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final LoadRegisterNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.reg, "reg");
        }};
    }

    @Override
    protected List<Object> getLeaves(LoadRegisterNode node) {
        return Collections.<Object>singletonList(node.type);
    }
}
