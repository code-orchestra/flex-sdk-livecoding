package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.RestParameterNode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class RestParameterNodeVisitor extends ParameterNodeVisitor<RestParameterNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final RestParameterNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.parameter, "parameter");
        }};
    }

    @Override
    protected List<Object> getLeaves(RestParameterNode node) {
        return Collections.emptyList();
    }
}
