package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.PackageNameNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class PackageNameNodeVisitor extends NodeVisitor<PackageNameNode> {
    @Override
    protected List<Node> getChildren(final PackageNameNode node) {
        return new ArrayList<Node>() {{
            add(node.url);
            add(node.id);
        }};
    }

    @Override
    protected List<Object> getLeaves(PackageNameNode node) {
        return Collections.emptyList();
    }
}
