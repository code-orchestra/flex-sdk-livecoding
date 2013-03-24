package codeOrchestra.tree.visitor;

import macromedia.asc.parser.IncludeDirectiveNode;

/**
 * @author Anton.I.Neverov
 */
public class IncludeDirectiveNodeVisitor extends NodeVisitor<IncludeDirectiveNode> {
    @Override
    protected StuffToCompare createStuffToCompare(IncludeDirectiveNode left, IncludeDirectiveNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
