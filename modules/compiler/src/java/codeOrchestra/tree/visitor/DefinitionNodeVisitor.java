package codeOrchestra.tree.visitor;

import macromedia.asc.parser.DefinitionNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public abstract class DefinitionNodeVisitor<N extends DefinitionNode> extends NodeVisitor<N> {
    @Override
    protected List<Node> getChildren(final N node) {
        return new ArrayList<Node>() {{
            add(node.attrs);
            add(node.metaData);
        }};
    }

    @Override
    protected List<Object> getLeaves(N node) {
        return Collections.<Object>singletonList(node.skip());
    }
}
