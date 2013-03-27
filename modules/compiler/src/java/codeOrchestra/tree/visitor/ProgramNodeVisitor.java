package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.ProgramNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ProgramNodeVisitor<N extends ProgramNode> extends NodeVisitor<N> {
    @Override
    protected List<Node> getChildren(final ProgramNode node) {
        return new ArrayList<Node>() {{
            add(node.statements);
        }};
    }

    @Override
    protected List<Object> getLeaves(ProgramNode node) {
        return Collections.emptyList();
    }
}
