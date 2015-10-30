package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ClassNameNode;
import macromedia.asc.parser.Node;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class ClassNameNodeVisitor extends NodeVisitor<ClassNameNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(final ClassNameNode node) {
        return new LinkedHashMap<Node, String>() {{
            put(node.pkgname, "pkgname");
            put(node.ident, "ident");
        }};
    }

    @Override
    protected List<Object> getLeaves(ClassNameNode node) {
        return Collections.emptyList();
    }
}
