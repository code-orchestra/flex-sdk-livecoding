package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.PragmaNode;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class PragmaNodeVisitor extends NodeVisitor<PragmaNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final PragmaNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.list, "list");
        }};
    }

    @Override
    protected List<Object> getLeaves(PragmaNode node) {
        return Collections.emptyList();
    }
}
