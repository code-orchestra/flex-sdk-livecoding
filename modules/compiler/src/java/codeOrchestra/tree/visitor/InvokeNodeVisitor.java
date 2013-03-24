package codeOrchestra.tree.visitor;

import macromedia.asc.parser.InvokeNode;

/**
 * @author Anton.I.Neverov
 */
public class InvokeNodeVisitor extends NodeVisitor<InvokeNode> {
    @Override
    protected StuffToCompare createStuffToCompare(InvokeNode left, InvokeNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
