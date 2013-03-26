package codeOrchestra.tree.visitor;

import macromedia.asc.parser.RegisterNode;

/**
 * @author Anton.I.Neverov
 */
public class RegisterNodeVisitor extends NodeVisitor<RegisterNode> {
    @Override
    protected StuffToCompare createStuffToCompare(RegisterNode left, RegisterNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftLeaves.add(left.index);
        stuffToCompare.rightLeaves.add(right.index);

        stuffToCompare.leftLeaves.add(left.type);
        stuffToCompare.rightLeaves.add(right.type);

        stuffToCompare.leftLeaves.add(left.void_result);
        stuffToCompare.rightLeaves.add(right.void_result);

        return stuffToCompare;
    }
}
