package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ParameterNode;

/**
 * @author Anton.I.Neverov
 */
public class ParameterNodeVisitor extends NodeVisitor<ParameterNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ParameterNode left, ParameterNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
