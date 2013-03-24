package codeOrchestra.tree.visitor;

import macromedia.asc.parser.DefinitionNode;

/**
 * @author Anton.I.Neverov
 */
public abstract class DefinitionNodeVisitor<N extends DefinitionNode> extends NodeVisitor<N> {
    @Override
    protected StuffToCompare createStuffToCompare(DefinitionNode left, DefinitionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

        stuffToCompare.leftChildren.add(left.attrs);
        stuffToCompare.rightChildren.add(right.attrs);

        stuffToCompare.leftChildren.add(left.metaData);
        stuffToCompare.rightChildren.add(right.metaData);

        // pkgdef is a back-reference, so we do not visit it

        stuffToCompare.leftLeaves.add(left.skip());
        stuffToCompare.rightLeaves.add(right.skip());

        return stuffToCompare;
    }
}
