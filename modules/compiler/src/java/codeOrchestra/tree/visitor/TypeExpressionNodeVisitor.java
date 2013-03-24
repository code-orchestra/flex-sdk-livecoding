package codeOrchestra.tree.visitor;

import macromedia.asc.parser.TypeExpressionNode;

/**
 * @author Anton.I.Neverov
 */
public class TypeExpressionNodeVisitor extends NodeVisitor<TypeExpressionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(TypeExpressionNode left, TypeExpressionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
