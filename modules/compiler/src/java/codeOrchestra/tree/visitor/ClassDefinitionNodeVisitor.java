package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ClassDefinitionNode;

/**
 * @author Anton.I.Neverov
 */
public class ClassDefinitionNodeVisitor<N extends ClassDefinitionNode> extends DefinitionNodeVisitor<N> {
    @Override
    protected StuffToCompare createStuffToCompare(ClassDefinitionNode left, ClassDefinitionNode right) {
        StuffToCompare stuffToCompare = super.createStuffToCompare(left, right);



        return stuffToCompare;
    }
}
