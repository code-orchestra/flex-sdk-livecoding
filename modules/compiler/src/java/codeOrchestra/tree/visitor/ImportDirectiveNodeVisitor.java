package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ImportDirectiveNode;

/**
 * @author Anton.I.Neverov
 */
public class ImportDirectiveNodeVisitor extends NodeVisitor<ImportDirectiveNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ImportDirectiveNode left, ImportDirectiveNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
