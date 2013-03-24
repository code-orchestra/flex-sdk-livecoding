package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ImportNode;

/**
 * @author Anton.I.Neverov
 */
public class ImportNodeVisitor extends NodeVisitor<ImportNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ImportNode left, ImportNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
