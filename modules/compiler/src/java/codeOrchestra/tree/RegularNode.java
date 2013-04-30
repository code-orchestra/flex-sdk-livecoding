package codeOrchestra.tree;

import codeOrchestra.tree.visitor.NodeVisitor;
import codeOrchestra.tree.visitor.NodeVisitorFactory;
import macromedia.asc.parser.Node;
import macromedia.asc.parser.PackageDefinitionNode;
import macromedia.asc.util.ObjectList;

import java.lang.reflect.Field;
import java.util.*;

/**
 * @author Anton.I.Neverov
 */
public class RegularNode {

    private String myRole;
    private RegularNode myParent;
    private final List<RegularNode> myChildren = new ArrayList<RegularNode>();

    public final Node myNode;

    public RegularNode(Node node, RegularNode parent) {
        this(node, (String) null);
        this.myParent = parent;
    }

    public RegularNode(Node node) {
        this(node, (String) null);
    }

    private RegularNode(Node node, String role) {
        myNode = node;
        myRole = role;

        NodeVisitor visitor = NodeVisitorFactory.getVisitor(node.getClass());
        LinkedHashMap<Node, String> children = visitor.getChildren(node);
        for (Node childNode : children.keySet()) {
            if (childNode == null) {
                continue;
            }
            if (childNode instanceof PackageDefinitionNode) {
                continue;
            }
            RegularNode regularChildNode = new RegularNode(childNode, children.get(childNode));
            regularChildNode.myParent = this;
            myChildren.add(regularChildNode);
        }
    }

    public void setParent(RegularNode myParent) {
        this.myParent = myParent;
    }

    public boolean isRoot() {
        return myParent == null;
    }

    public boolean isSingleChild() {
        try {
            return isSingleField(myParent.myNode.getClass().getField(myRole));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean isSingleField(String role) {
        try {
            return isSingleField(myNode.getClass().getField(role));
        } catch (NoSuchFieldException e) {
            throw new RuntimeException();
        }
    }

    private boolean isSingleField(Field field) {
        if (field.getType().isAssignableFrom(Node.class)) {
            return true;
        } else if (field.getType().isAssignableFrom(ObjectList.class)) {
            return false;
        } else {
            throw new RuntimeException();
        }
    }

    public RegularNode detach() {
        try {
            Node parentNode = myParent.myNode;
            Field field = parentNode.getClass().getField(myRole);
            if (isSingleChild()) {
                Object o = field.get(parentNode);
                if (o != myNode) {
                    throw new RuntimeException();
                }
                field.set(parentNode, null);
            } else {
                Object o = field.get(parentNode);
                ObjectList list = (ObjectList) o;
                if (!list.contains(myNode)) {
                    throw new RuntimeException();
                }
                list.remove(myNode);
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        myParent.myChildren.remove(this);
        return this;
    }

    public RegularNode replace(Node node) {
        RegularNode regularNode;
        if (isSingleChild()) {
            detach();
            regularNode = myParent.setChild(node, myRole);
        } else {
            int index = getIndex();
            detach();
            regularNode = myParent.insertChild(node, myRole, index);
        }
        return regularNode;
    }

    public RegularNode setChild(Node node, String role) {
        try {
            Field field = myNode.getClass().getField(role);
            if (isSingleField(field)) {
                field.set(myNode, node);
            } else {
                throw new RuntimeException();
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        RegularNode regularNode = new RegularNode(node, role);
        myChildren.add(regularNode);
        return regularNode;
    }

    public RegularNode addChild(Node node, String role) {
        return insertChild(node, role, -1);
    }

    public RegularNode insertChild(Node node, String role, int index) {
        try {
            Field field = myNode.getClass().getField(role);
            if (!isSingleField(field)) {
                Object o = field.get(myNode);
                ObjectList list = (ObjectList) o;
                if (index == -1) {
                    list.add(node);
                } else {
                    list.add(index, node);
                }
            } else {
                throw new RuntimeException();
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }

        RegularNode regularNode = new RegularNode(node, role);
        myChildren.add(regularNode);
        return regularNode;
    }

    public int getIndex() {
        return myParent.getChildIndex(this);
    }

    public int getChildIndex(RegularNode node) {
        try {
            Field field = myNode.getClass().getField(node.myRole);
            if (!isSingleField(field)) {
                Object o = field.get(myNode);
                ObjectList list = (ObjectList) o;
                return list.indexOf(node.myNode);
            } else {
                throw new RuntimeException();
            }
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public RegularNode getParent() {
        return myParent;
    }

    public int getChildCount(String role) {
        return getChildren(role).size();
    }

    public RegularNode getChild(String role) {
        for (RegularNode myChild : myChildren) {
            if (myChild.myRole.equals(role)) {
                return myChild;
            }
        }
        return null;
    }

    public Class getNodeClass() {
        return myNode.getClass();
    }

    public Node getASTNode() {
        return myNode;
    }

    public List<RegularNode> getChildren() {
        return myChildren;
    }

    public List<RegularNode> getChildren(String role) {
        ArrayList<RegularNode> nodes = new ArrayList<RegularNode>();
        for (RegularNode myChild : myChildren) {
            if (myChild.myRole.equals(role)) {
                nodes.add(myChild);
            }
        }
        return nodes;
    }

    public List<RegularNode> getDescendants() {
        return getDescendants(Collections.<Class>emptySet());
    }

    public List<RegularNode> getDescendants(Class filter) {
        return getDescendants(Collections.singleton(filter));
    }

    public List<RegularNode> getDescendants(Class filter, Class stopFilter) {
        return getDescendants(Collections.singleton(filter), Collections.singleton(stopFilter));
    }

    public List<RegularNode> getDescendants(Set<Class> filter) {
        return getDescendants(filter, Collections.<Class>emptySet());
    }

    public List<RegularNode> getDescendants(Set<Class> filter, Set<Class> stopFilter) {
        Queue<RegularNode> nodes = new LinkedList<RegularNode>();
        nodes.add(this);

        ArrayList<RegularNode> result = new ArrayList<RegularNode>();
        RegularNode node;

        while ((node = nodes.poll()) != null) {
            if (!stopFilter.isEmpty() && stopFilter.contains(node.getNodeClass())) {
                continue;
            }
            if (filter.isEmpty() || filter.contains(node.getNodeClass())) {
                result.add(node);
            }

            nodes.addAll(node.getChildren());
        }

        return result;
    }

}
