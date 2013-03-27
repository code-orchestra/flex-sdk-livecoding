package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
import macromedia.asc.parser.ParameterListNode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ParameterListNodeVisitor extends NodeVisitor<ParameterListNode> {
    @Override
    protected List<Node> getChildren(ParameterListNode node) {
        return new ArrayList<Node>(node.items);
    }

    @Override
    protected List<Object> getLeaves(final ParameterListNode node) {
        return new ArrayList<Object>() {{
            add(node.types);
            add(node.decl_styles);
        }};
    }
}
