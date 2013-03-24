package codeOrchestra.tree.visitor;

import macromedia.asc.parser.RestParameterNode;

/**
 * @author Anton.I.Neverov
 */
public class RestParameterNodeVisitor extends NodeVisitor<RestParameterNode> {
    @Override
    protected StuffToCompare createStuffToCompare(RestParameterNode left, RestParameterNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
