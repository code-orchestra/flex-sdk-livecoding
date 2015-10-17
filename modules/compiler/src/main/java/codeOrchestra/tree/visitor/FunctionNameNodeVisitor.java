package codeOrchestra.tree.visitor;

import macromedia.asc.parser.FunctionNameNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class FunctionNameNodeVisitor extends NodeVisitor<FunctionNameNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final FunctionNameNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.identifier, "identifier");
        }};
    }

    @Override
    protected List<Object> getLeaves(FunctionNameNode node) {
        return Collections.<Object>singletonList(node.kind);
    }
}
