package codeOrchestra.tree.visitor;

import macromedia.asc.parser.FunctionSignatureNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class FunctionSignatureNodeVisitor extends NodeVisitor<FunctionSignatureNode> {
    @Override
    protected List<Node> getChildren(final FunctionSignatureNode node) {
        return new ArrayList<Node>() {{
            add(node.parameter);
            add(node.result);
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
