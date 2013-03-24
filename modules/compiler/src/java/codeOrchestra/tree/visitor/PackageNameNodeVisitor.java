package codeOrchestra.tree.visitor;

import macromedia.asc.parser.PackageNameNode;

/**
 * @author Anton.I.Neverov
 */
public class PackageNameNodeVisitor extends NodeVisitor<PackageNameNode> {
    @Override
    protected StuffToCompare createStuffToCompare(PackageNameNode left, PackageNameNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
