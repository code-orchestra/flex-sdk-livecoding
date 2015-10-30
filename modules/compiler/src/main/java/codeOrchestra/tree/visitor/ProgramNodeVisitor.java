package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.ProgramNode;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class ProgramNodeVisitor<N extends ProgramNode> extends NodeVisitor<N> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final N node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.statements, "statements");
        }};
    }

    @Override
    protected List<Object> getLeaves(N node) {
        return Collections.emptyList();
    }
}
