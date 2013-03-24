package codeOrchestra.tree.visitor;

import macromedia.asc.parser.PackageDefinitionNode;

/**
 * @author Anton.I.Neverov
 */
public class PackageDefinitionNodeVisitor extends NodeVisitor<PackageDefinitionNode> {
    @Override
    protected StuffToCompare createStuffToCompare(PackageDefinitionNode left, PackageDefinitionNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
