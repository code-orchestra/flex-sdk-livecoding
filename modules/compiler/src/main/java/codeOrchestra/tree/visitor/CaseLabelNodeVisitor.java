package codeOrchestra.tree.visitor;

import macromedia.asc.parser.CaseLabelNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class CaseLabelNodeVisitor extends NodeVisitor<CaseLabelNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final CaseLabelNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.label, "label");
        }};
    }

    @Override
    protected List<Object> getLeaves(CaseLabelNode node) {
        return Collections.emptyList();
    }
}
