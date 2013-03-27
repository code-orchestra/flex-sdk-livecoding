package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.RestParameterNode;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class RestParameterNodeVisitor extends ParameterNodeVisitor<RestParameterNode> {
    @Override
    protected List<Node> getChildren(RestParameterNode node) {
        return Collections.singletonList(node.parameter);
    }

    @Override
    protected List<Object> getLeaves(RestParameterNode node) {
        return Collections.emptyList();
    }
}
