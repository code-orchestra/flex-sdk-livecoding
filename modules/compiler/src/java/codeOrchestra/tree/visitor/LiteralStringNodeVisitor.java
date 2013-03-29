package codeOrchestra.tree.visitor;

import macromedia.asc.parser.LiteralStringNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class LiteralStringNodeVisitor extends NodeVisitor<LiteralStringNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(LiteralStringNode node) {
        return emptyMap;
    }

    @Override
    protected List<Object> getLeaves(LiteralStringNode node) {
        return Collections.<Object>singletonList(node.value);
    }
}
