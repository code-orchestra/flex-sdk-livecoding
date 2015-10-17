package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.PackageNameNode;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class PackageNameNodeVisitor extends NodeVisitor<PackageNameNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final PackageNameNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.url, "url");
            put(node.id, "id");
        }};
    }

    @Override
    protected List<Object> getLeaves(PackageNameNode node) {
        return Collections.emptyList();
    }
}
