package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ImportNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ImportNodeVisitor extends NodeVisitor<ImportNode> {
    @Override
    protected List<Node> getChildren(ImportNode node) {
        return Collections.<Node>singletonList(node.filespec);
    }

    @Override
    protected List<Object> getLeaves(ImportNode node) {
        return Collections.emptyList();
    }
}
