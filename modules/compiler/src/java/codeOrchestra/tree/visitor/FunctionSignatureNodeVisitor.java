package codeOrchestra.tree.visitor;

import macromedia.asc.parser.FunctionSignatureNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class FunctionSignatureNodeVisitor extends NodeVisitor<FunctionSignatureNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final FunctionSignatureNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.parameter, "parameter");
            put(node.result, "result");
        }};
    }

    @Override
    protected List<Object> getLeaves(final FunctionSignatureNode node) {
        return new ArrayList<Object>() {{
            add(node.type);
            add(node.typeref);
        }};
    }
}
