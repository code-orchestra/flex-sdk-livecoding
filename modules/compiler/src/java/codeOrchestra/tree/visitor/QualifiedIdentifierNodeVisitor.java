package codeOrchestra.tree.visitor;

import macromedia.asc.parser.QualifiedIdentifierNode;

/**
 * @author Anton.I.Neverov
 */
public class QualifiedIdentifierNodeVisitor<N extends QualifiedIdentifierNode> extends IdentifierNodeVisitor<N> {
    @Override
    protected StuffToCompare createStuffToCompare(QualifiedIdentifierNode left, QualifiedIdentifierNode right) {
        StuffToCompare stuffToCompare = super.createStuffToCompare(left, right);

        stuffToCompare.leftChildren.add(left.qualifier);
        stuffToCompare.rightChildren.add(right.qualifier);

        stuffToCompare.leftLeaves.add(left.is_config_name);
        stuffToCompare.rightLeaves.add(right.is_config_name);

        return stuffToCompare;
    }
}
