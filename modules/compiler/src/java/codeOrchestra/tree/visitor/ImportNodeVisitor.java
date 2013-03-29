package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ImportNode;
import macromedia.asc.parser.Node;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ImportNodeVisitor extends NodeVisitor<ImportNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final ImportNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.filespec, "filespec");
        }};
    }

    @Override
    protected List<Object> getLeaves(ImportNode node) {
        return Collections.emptyList();
    }
}
