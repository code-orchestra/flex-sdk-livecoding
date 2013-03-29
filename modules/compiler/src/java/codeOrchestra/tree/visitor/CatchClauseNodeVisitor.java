package codeOrchestra.tree.visitor;

import macromedia.asc.parser.CatchClauseNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class CatchClauseNodeVisitor extends NodeVisitor<CatchClauseNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final CatchClauseNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.parameter, "parameter");
            put(node.statements, "statements");
        }};
    }

    @Override
    protected List<Object> getLeaves(final CatchClauseNode node) {
        return new ArrayList<Object>() {{
            add(node.typeref);
            add(node.finallyInserted);
            add(node.default_namespace);
            add(node.activation);
        }};
    }
}
