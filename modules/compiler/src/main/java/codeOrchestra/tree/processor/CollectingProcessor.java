package codeOrchestra.tree.processor;

import macromedia.asc.parser.Node;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Anton.I.Neverov
 */
public class CollectingProcessor implements INodeProcessor {

    private Set<Class> filter;
    private List<Node> nodes = new ArrayList<>();

    public CollectingProcessor(Set<Class> filter) {
        this.filter = filter;
    }

    @Override
    public void process(Node node) {
        if (filter.isEmpty() || filter.contains(node.getClass())) {
            nodes.add(node);
        }
    }

    public List<Node> getNodes() {
        return nodes;
    }
}
