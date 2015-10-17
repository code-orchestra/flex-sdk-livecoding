package codeOrchestra.tree.visitor;

import macromedia.asc.parser.FinallyClauseNode;
import macromedia.asc.parser.Node;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class FinallyClauseNodeVisitor extends NodeVisitor<FinallyClauseNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final FinallyClauseNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.statements, "statements");
            put(node.default_catch, "default_catch");
        }};
    }

    @Override
    protected List<Object> getLeaves(FinallyClauseNode node) {
        return Collections.emptyList();
    }
}
