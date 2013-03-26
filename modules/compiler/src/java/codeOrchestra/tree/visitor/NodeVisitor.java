package codeOrchestra.tree.visitor;

import macromedia.asc.parser.Node;
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

    private Map<N, Integer> visitedNodes = new HashMap<N, Integer>();
    private static boolean testMode = false;

    /**
     * This is implemented only for comparing function bodies!
     */
    public boolean compareTrees(N left, N right) {
        if (testMode) {
            // Infinite recursion check
            if (visitedNodes.containsKey(left)) {
                visitedNodes.put(left, visitedNodes.get(left) + 1);
            } else {
                visitedNodes.put(left, 0);
            }
            if (visitedNodes.get(left) > 30) {
                throw new RuntimeException();
            }
        }
        StuffToCompare stuffToCompare = createStuffToCompare(left, right);
        return compare(stuffToCompare);
    }

    protected abstract StuffToCompare createStuffToCompare(N left, N right);

    protected boolean compare(StuffToCompare stuffToCompare) {
        if (stuffToCompare.leftLeaves.size() != stuffToCompare.rightLeaves.size()) {
            return false;
        }
        if (stuffToCompare.leftChildren.size() != stuffToCompare.rightChildren.size()) {
            return false;
        }
        for (int i = 0; i < stuffToCompare.leftLeaves.size(); i++) {
            if (!compareObjects(stuffToCompare.leftLeaves.get(i), stuffToCompare.rightLeaves.get(i))) {
                return false;
            }
        }
        for (int i = 0; i < stuffToCompare.leftChildren.size(); i++) {
            Node leftChild = stuffToCompare.leftChildren.get(i);
            Node rightChild = stuffToCompare.rightChildren.get(i);
            if (leftChild == null && rightChild == null) {
                continue;
            }
            if (leftChild == null || rightChild == null) {
                return false;
            }
            if (leftChild.getClass() != rightChild.getClass()) {
                return false;
            }
            NodeVisitor childVisitor = NodeVisitorFactory.getVisitor(leftChild.getClass());
            if (!childVisitor.compareTrees(leftChild, rightChild)) {
                return false;
            }
        }
        return true;
    }

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
        return left.equals(right);
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
            return false;
        }
        if (left.values.length != right.values.length) {
            return false;
        }
        for (int i = 0; i < left.values.length; i++) {
            if (!compareObjects(left.values[i], right.values[i])) {
                return false;
            }
        }
        return true;
    }

    protected static class StuffToCompare {
        final List<Node> leftChildren = new ArrayList<Node>();
        final List<Node> rightChildren = new ArrayList<Node>();
        final List<Object> leftLeaves = new ArrayList<Object>();
        final List<Object> rightLeaves = new ArrayList<Object>();
    }
}
