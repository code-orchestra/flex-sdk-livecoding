package codeOrchestra.tree.visitor;

import macromedia.asc.parser.BinaryProgramNode;
import macromedia.asc.parser.Node;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class BinaryProgramNodeVisitor extends ProgramNodeVisitor<BinaryProgramNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(BinaryProgramNode node) {
        return super.getChildren(node);
    }

    @Override
    protected List<Object> getLeaves(BinaryProgramNode node) {
        return super.getLeaves(node);
    }
}
