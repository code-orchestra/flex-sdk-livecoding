package codeOrchestra.tree.visitor;

import macromedia.asc.parser.ErrorNode;
import macromedia.asc.parser.Node;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class ErrorNodeVisitor extends NodeVisitor<ErrorNode> {
    @Override
    public LinkedHashMap<Node, String> getChildren(ErrorNode node) {
        return emptyMap;
    }

    @Override
    protected List<Object> getLeaves(final ErrorNode node) {
        return new ArrayList<Object>() {{
            add(node.errorArg);
            add(node.errorCode);
        }};
    }
}
