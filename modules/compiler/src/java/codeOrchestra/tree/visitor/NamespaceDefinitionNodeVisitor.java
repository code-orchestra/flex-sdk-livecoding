package codeOrchestra.tree.visitor;

import macromedia.asc.parser.NamespaceDefinitionNode;

/**
 * @author Anton.I.Neverov
 */
public class NamespaceDefinitionNodeVisitor extends NodeVisitor<NamespaceDefinitionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(NamespaceDefinitionNode left, NamespaceDefinitionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
