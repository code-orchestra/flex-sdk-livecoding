package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ClassNameNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ClassNameNodeVisitor extends NodeVisitor<ClassNameNode> {
    @Override
    protected List<Node> getChildren(final ClassNameNode node) {
        return new ArrayList<Node>() {{
            add(node.pkgname);
            add(node.ident);
        }};
    }

    @Override
    protected List<Object> getLeaves(ClassNameNode node) {
        return Collections.emptyList();
    }
}
