package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ProgramNode;

/**
 * @author Anton.I.Neverov
 */
public class ProgramNodeVisitor extends NodeVisitor<ProgramNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ProgramNode left, ProgramNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
