package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.PackageIdentifiersNode;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class PackageIdentifiersNodeVisitor extends NodeVisitor<PackageIdentifiersNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final PackageIdentifiersNode node) {
        return new LinkedHashMap<Node, String>() {{
            for (Node item : node.list) {
                put(item, "list");
            }
        }};
    }

    @Override
    protected List<Object> getLeaves(PackageIdentifiersNode node) {
        return Collections.emptyList();
    }
}
