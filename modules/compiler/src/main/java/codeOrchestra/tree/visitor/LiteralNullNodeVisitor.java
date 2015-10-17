package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralNullNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class LiteralNullNodeVisitor extends NodeVisitor<LiteralNullNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(LiteralNullNode node) {
        return emptyMap;
    }

    @Override
    protected List<Object> getLeaves(LiteralNullNode node) {
        return Collections.emptyList();
    }
}
