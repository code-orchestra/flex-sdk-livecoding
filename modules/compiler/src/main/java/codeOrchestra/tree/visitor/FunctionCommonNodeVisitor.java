package codeOrchestra.tree.visitor;

import macromedia.asc.parser.FunctionCommonNode;
import macromedia.asc.parser.Node;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class FunctionCommonNodeVisitor extends NodeVisitor<FunctionCommonNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final FunctionCommonNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.identifier, "identifier");
            put(node.signature, "signature");
            put(node.body, "body");
        }};
    }

    @Override
    protected List<Object> getLeaves(FunctionCommonNode node) {
        return Collections.<Object>singletonList(node.kind);
    }
}
