package codeOrchestra.tree.visitor;

import macromedia.asc.parser.TypedIdentifierNode;

/**
 * @author Anton.I.Neverov
 */
public class TypedIdentifierNodeVisitor extends NodeVisitor<TypedIdentifierNode> {
    @Override
    protected StuffToCompare createStuffToCompare(TypedIdentifierNode left, TypedIdentifierNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
