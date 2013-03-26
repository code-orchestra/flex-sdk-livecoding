package codeOrchestra.tree.visitor;

import macromedia.asc.parser.RestParameterNode;

/**
 * @author Anton.I.Neverov
 */
public class RestParameterNodeVisitor extends ParameterNodeVisitor<RestParameterNode> {
    @Override
    protected StuffToCompare createStuffToCompare(RestParameterNode left, RestParameterNode right) {
        StuffToCompare stuffToCompare = super.createStuffToCompare(left, right);

        stuffToCompare.leftChildren.add(left.parameter);
        stuffToCompare.rightChildren.add(right.parameter);

        return stuffToCompare;
    }
}
