package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.PackageIdentifiersNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class PackageIdentifiersNodeVisitor extends NodeVisitor<PackageIdentifiersNode> {
    @Override
    protected List<Node> getChildren(PackageIdentifiersNode node) {
        return new ArrayList<Node>(node.list);
    }

    @Override
    protected List<Object> getLeaves(PackageIdentifiersNode node) {
        return Collections.emptyList();
    }
}
