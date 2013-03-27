package codeOrchestra.tree.visitor;

import macromedia.asc.parser.FinallyClauseNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class FinallyClauseNodeVisitor extends NodeVisitor<FinallyClauseNode> {
    @Override
    protected List<Node> getChildren(final FinallyClauseNode node) {
        return new ArrayList<Node>() {{
            add(node.statements);
            add(node.default_catch);
        }};
    }

    @Override
    protected List<Object> getLeaves(FinallyClauseNode node) {
        return Collections.emptyList();
    }
}
