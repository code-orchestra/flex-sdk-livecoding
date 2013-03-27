package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ErrorNode;
import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Anton.I.Neverov
 */
public class ErrorNodeVisitor extends NodeVisitor<ErrorNode> {
    @Override
    protected List<Node> getChildren(ErrorNode node) {
        return Collections.emptyList();
    }

    @Override
    protected List<Object> getLeaves(final ErrorNode node) {
        return new ArrayList<Object>() {{
            add(node.errorArg);
            add(node.errorCode);
        }};
    }
}
