package codeOrchestra.tree.visitor;

import macromedia.asc.parser.EmptyStatementNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class EmptyStatementNodeVisitor extends NodeVisitor<EmptyStatementNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(EmptyStatementNode node) {
        return emptyMap;
    }

    @Override
    protected List<Object> getLeaves(EmptyStatementNode node) {
        return Collections.emptyList();
    }
}
