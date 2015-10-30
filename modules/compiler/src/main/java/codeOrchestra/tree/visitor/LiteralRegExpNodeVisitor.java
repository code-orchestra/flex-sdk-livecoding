package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralRegExpNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class LiteralRegExpNodeVisitor extends NodeVisitor<LiteralRegExpNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(LiteralRegExpNode node) {
        return emptyMap;
    }

    @Override
    protected List<Object> getLeaves(LiteralRegExpNode node) {
        return Collections.<Object>singletonList(node.value);
    }
}
