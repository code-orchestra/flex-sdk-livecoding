package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ParameterListNode;

/**
 * @author Anton.I.Neverov
 */
public class ParameterListNodeVisitor extends NodeVisitor<ParameterListNode> {
    @Override
    protected StuffToCompare createStuffToCompare(ParameterListNode left, ParameterListNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
