package codeOrchestra.tree.visitor;

import macromedia.asc.parser.UntypedVariableBindingNode;

/**
 * @author Anton.I.Neverov
 */
public class UntypedVariableBindingNodeVisitor extends NodeVisitor<UntypedVariableBindingNode> {
    @Override
    protected StuffToCompare createStuffToCompare(UntypedVariableBindingNode left, UntypedVariableBindingNode right) {
        StuffToCompare stuffToCompare = new StuffToCompare();

		

        return stuffToCompare;
    }
}
