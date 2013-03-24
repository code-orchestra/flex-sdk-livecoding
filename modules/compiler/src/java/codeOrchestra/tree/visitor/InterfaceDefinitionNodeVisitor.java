package codeOrchestra.tree.visitor;

import macromedia.asc.parser.InterfaceDefinitionNode;

/**
 * @author Anton.I.Neverov
 */
public class InterfaceDefinitionNodeVisitor extends NodeVisitor<InterfaceDefinitionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(InterfaceDefinitionNode left, InterfaceDefinitionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
