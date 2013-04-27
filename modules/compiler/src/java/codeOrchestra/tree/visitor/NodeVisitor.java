package codeOrchestra.tree.visitor;

import codeOrchestra.tree.processor.CollectingProcessor;
import codeOrchestra.tree.processor.INodeProcessor;
import macromedia.asc.parser.MetaDataEvaluator;
import macromedia.asc.parser.Node;
import macromedia.asc.parser.PackageDefinitionNode;
import macromedia.asc.parser.ProgramNode;
import macromedia.asc.semantics.MetaData;
import macromedia.asc.semantics.ObjectValue;
import macromedia.asc.semantics.ReferenceValue;
import macromedia.asc.semantics.TypeInfo;
import macromedia.asc.util.NumberConstant;
import macromedia.asc.util.NumberUsage;

import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public abstract class NodeVisitor<N extends Node> {

    private static Map<Node, Integer> visitedNodes = new HashMap<Node, Integer>();
    private static boolean testMode = true;
    protected static final LinkedHashMap<Node, String> emptyMap = new LinkedHashMap<Node, String>();

    /**
     * This is implemented only for comparing function bodies!
     */
    public boolean compareTrees(N left, N right) {
        if (testMode) {
            checkInfiniteRecursion(left);
        }

        // We use LinkedHashMap, so keyset iteration goes in the same order as nodes were inserted
        List<Node> leftChildren = new ArrayList<Node>(getChildren(left).keySet());
        List<Node> rightChildren = new ArrayList<Node>(getChildren(right).keySet());
        List<Object> leftLeaves = getLeaves(left);
        List<Object> rightLeaves = getLeaves(right);

        if (leftLeaves == null || rightLeaves == null) {
            throw new RuntimeException();
        }

        if (leftLeaves.size() != rightLeaves.size()) {
            reportDifference(leftLeaves, rightLeaves, 1);
            return false;
        }
        if (leftChildren.size() != rightChildren.size()) {
            reportDifference(leftChildren, rightChildren, 2);
            return false;
        }
        for (int i = 0; i < leftLeaves.size(); i++) {
            if (!compareObjects(leftLeaves.get(i), rightLeaves.get(i))) {
                reportDifference(leftLeaves.get(i), rightLeaves.get(i), 3);
                return false;
            }
        }
        for (int i = 0; i < leftChildren.size(); i++) {
            Node leftChild = leftChildren.get(i);
            Node rightChild = rightChildren.get(i);
            if (leftChild == null && rightChild == null) {
                continue;
            }
            if (leftChild == null || rightChild == null) {
                reportDifference(leftChild, rightChild, 4);
                return false;
            }
            if (leftChild.getClass() != rightChild.getClass()) {
                reportDifference(leftChild.getClass(), rightChild.getClass(), 5);
                return false;
            }
            NodeVisitor childVisitor = NodeVisitorFactory.getVisitor(leftChild.getClass());
            if (!childVisitor.compareTrees(leftChild, rightChild)) {
                return false;
            }
        }

        if (testMode) {
            visitedNodes.clear();
        }

        return true;
    }

    private void reportDifference(Object left, Object right, int code) {
        if (!testMode) {
            return;
        }
        System.out.println("Difference [" + code + "] Left: " + getInfo(left) + ", Right: " + getInfo(right));
    }

    private String getInfo(Object obj) {
        if (obj == null) {
            return "null";
        }
        return obj.getClass().getSimpleName() + "->(" + obj.toString() + ")";
    }

    public static void applyToTree(Node treeRoot, INodeProcessor nodeProcessor) {
        Queue<Node> nodesToProcess = new LinkedList<Node>();
        if (treeRoot instanceof ProgramNode) {
            treeRoot = ((ProgramNode) treeRoot).pkgdefs.get(0);
        }
        nodesToProcess.add(treeRoot);

        Node node;
        while ((node = nodesToProcess.poll()) != null) {
            if (testMode) {
                checkInfiniteRecursion(node);
            }
            nodeProcessor.process(node);
            NodeVisitor visitor = NodeVisitorFactory.getVisitor(node.getClass());
            Set<Node> nodeSet = visitor.getChildren(node).keySet();
            for (Node child : nodeSet) {
                if (child != null && !(child instanceof PackageDefinitionNode)) { // PackageDefinitionNode contains itself in its statements
                    nodesToProcess.add(child);
                }
            }
        }

        if (testMode) {
            visitedNodes.clear();
        }
    }

    private static void checkInfiniteRecursion(Node node) {
        if (visitedNodes.containsKey(node)) {
            visitedNodes.put(node, visitedNodes.get(node) + 1);
        } else {
            visitedNodes.put(node, 0);
        }
        if (visitedNodes.get(node) > 30) {
            throw new RuntimeException();
        }
    }

    public static List<Node> getDescendants(Node treeRoot) {
        return getDescendants(treeRoot, Collections.<Class>emptySet());
    }

    public static List<Node> getDescendants(Node treeRoot, Class nodeClass) {
        return getDescendants(treeRoot, Collections.singleton(nodeClass));
    }

    public static List<Node> getDescendants(Node treeRoot, Set<Class> nodeClasses) {
        CollectingProcessor collectingProcessor = new CollectingProcessor(nodeClasses);
        applyToTree(treeRoot, collectingProcessor);
        return collectingProcessor.getNodes();
    }

    // TODO: It does not return children that are known to be null right after parse1
    public abstract LinkedHashMap<Node, String> getChildren(N node);

    // TODO: It does not return leaves for nodes higher in tree than FunctionDefinitionNode
    protected abstract List<Object> getLeaves(N node);

    protected boolean compareObjects(Object left, Object right) {
        if (left == null && right == null) {
            return true;
        }
        if (left == null || right == null) {
            return false;
        }
        if (left instanceof Node || right instanceof Node) {
            throw new RuntimeException();
        }
        if (left.getClass() != right.getClass()) {
            return false;
        }
        if (left instanceof NumberUsage) {
            return compareNumberUsages(((NumberUsage) left), (NumberUsage) right);
        }
        if (left instanceof NumberConstant) {
            return compareNumberConstants(((NumberConstant) left), (NumberConstant) right);
        }
        if (left instanceof TypeInfo) {
            return compareTypeInfos(((TypeInfo) left), (TypeInfo) right);
        }
        if (left instanceof ObjectValue) {
            return compareObjectValues(((ObjectValue) left), (ObjectValue) right);
        }
        if (left instanceof ReferenceValue) {
            return compareReferenceValues(((ReferenceValue) left), (ReferenceValue) right);
        }
        if (left instanceof MetaData) {
            return compareMetaDatas(((MetaData) left), (MetaData) right);
        }
        if (left instanceof MetaDataEvaluator.KeyValuePair) {
            return compareKeyValuePairs((MetaDataEvaluator.KeyValuePair) left, (MetaDataEvaluator.KeyValuePair) right);
        }
        return left.equals(right);
    }

    private boolean compareKeyValuePairs(MetaDataEvaluator.KeyValuePair left, MetaDataEvaluator.KeyValuePair right) {
        return equals(left.key, right.key) && equals(left.obj, right.obj);
    }

    private boolean compareNumberUsages(NumberUsage left, NumberUsage right) {
        if (left.get_usage() != right.get_usage()) {
            return false;
        }
        if (left.get_rounding() != right.get_rounding()) {
            return false;
        }
        if (left.get_precision() != right.get_precision()) {
            return false;
        }
        if (left.get_floating_usage() != right.get_floating_usage()) {
            return false;
        }
        return true;
    }

    private boolean compareTypeInfos(TypeInfo left, TypeInfo right) {
        if (left.isNullable() != right.isNullable()) {
            return false;
        }
        if (!compareObjects(left.getTypeValue(), right.getTypeValue())) {
            return false;
        }
        if (!compareObjects(left.getPrototype(), right.getPrototype())) {
            return false;
        }
        if (!compareObjects(left.getName(), right.getName())) {
            return false;
        }
        return true;
    }

    private boolean compareObjectValues(ObjectValue left, ObjectValue right) {
        return left.compareTo(right) == 0; // TODO: check this
    }

    private boolean compareReferenceValues(ReferenceValue left, ReferenceValue right) {
        if (!compareObjects(left.getBase(), right.getBase())) {
            return false;
        }
        if (!compareObjects(left.getType(), right.getType())) {
            return false;
        }
        if (!compareObjects(left.name, right.name)) {
            return false;
        }
        // TODO: Other fields?
        return true;
    }

    private boolean compareNumberConstants(NumberConstant left, NumberConstant right) {
        return left.doubleValue() == right.doubleValue();
    }

    private boolean compareMetaDatas(MetaData left, MetaData right) {
        if (!compareObjects(left.id, right.id)) {
            return false;
        }
        if (left.values == null && right.values == null) {
            return true;
        }
        if (left.values == null || right.values == null) {
            reportDifference(left.values, right.values, 6);
            return false;
        }
        if (left.values.length != right.values.length) {
            reportDifference(left.values, right.values, 7);
            return false;
        }
        for (int i = 0; i < left.values.length; i++) {
            if (!compareObjects(left.values[i], right.values[i])) {
                reportDifference(left.values[i], right.values[i], 8);
                return false;
            }
        }
        return true;
    }

    public static boolean equals(Object object1, Object object2) {
        if (object1 == object2) {
            return true;
        }
        if ((object1 == null) || (object2 == null)) {
            return false;
        }
        return object1.equals(object2);
    }
}
