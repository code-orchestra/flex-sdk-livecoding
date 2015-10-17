package codeOrchestra.tree.visitor;

import macromedia.asc.parser.BinaryClassDefNode;
import macromedia.asc.parser.Node;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class BinaryClassDefNodeVisitor extends ClassDefinitionNodeVisitor<BinaryClassDefNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(BinaryClassDefNode node) {
        return super.getChildren(node);
    }

    @Override
    protected List<Object> getLeaves(BinaryClassDefNode node) {
        return super.getLeaves(node);
    }
}
