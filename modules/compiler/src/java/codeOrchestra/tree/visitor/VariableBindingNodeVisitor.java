package codeOrchestra.tree.visitor;

import macromedia.asc.parser.VariableBindingNode;

/**
 * @author Anton.I.Neverov
 */
public class VariableBindingNodeVisitor extends NodeVisitor<VariableBindingNode> {
    @Override
    protected StuffToCompare createStuffToCompare(VariableBindingNode left, VariableBindingNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
