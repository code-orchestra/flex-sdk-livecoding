package codeOrchestra.tree.visitor;

import macromedia.asc.parser.BinaryProgramNode;

/**
 * @author Anton.I.Neverov
 */
public class BinaryProgramNodeVisitor extends NodeVisitor<BinaryProgramNode> {
    @Override
    protected StuffToCompare createStuffToCompare(BinaryProgramNode left, BinaryProgramNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
