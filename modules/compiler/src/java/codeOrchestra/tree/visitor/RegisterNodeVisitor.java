package codeOrchestra.tree.visitor;

import macromedia.asc.parser.RegisterNode;

/**
 * @author Anton.I.Neverov
 */
public class RegisterNodeVisitor extends NodeVisitor<RegisterNode> {
    @Override
    protected StuffToCompare createStuffToCompare(RegisterNode left, RegisterNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
