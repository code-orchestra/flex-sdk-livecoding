package codeOrchestra.tree.visitor;

import macromedia.asc.parser.PackageIdentifiersNode;

/**
 * @author Anton.I.Neverov
 */
public class PackageIdentifiersNodeVisitor extends NodeVisitor<PackageIdentifiersNode> {
    @Override
    protected StuffToCompare createStuffToCompare(PackageIdentifiersNode left, PackageIdentifiersNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
