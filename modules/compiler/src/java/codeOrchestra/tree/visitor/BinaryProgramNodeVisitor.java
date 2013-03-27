package codeOrchestra.tree.visitor;

import macromedia.asc.parser.BinaryProgramNode;
import macromedia.asc.parser.Node;

import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class BinaryProgramNodeVisitor extends ProgramNodeVisitor<BinaryProgramNode> {
    @Override
    protected List<Node> getChildren(BinaryProgramNode node) {
        return super.getChildren(node);
    }

    @Override
    protected List<Object> getLeaves(BinaryProgramNode node) {
        return super.getLeaves(node);
    }
}
