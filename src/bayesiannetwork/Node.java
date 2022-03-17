package bayesiannetwork;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a node (or variable / event) in a Bayesian network.
 * */
public class Node {

    /**
     * Name / label of the node.
     * */
    private final String label;

    /**
     * List of parents of the node.
     * */
    private final List<Node> parents = new ArrayList<>();

    /**
     * List of children of the node.
     * */
    private final List<Node> children = new ArrayList<>();

    /**
     * Domain of the node. For this implementation, the domain is always boolean
     * but this field was added to future-prove the implementation for a possible extension.
     * */
    private final int[] domain = {1, 0};

    /**
     * Conditional probability table for the node.
     * */
    private final Factor table = new Factor(this);

    public Node(String label) {
        this.label = label;
    }

    public List<Node> getParents() {
        return parents;
    }

    public List<Node> getChildren() {
        return children;
    }

    public String getLabel() {
        return label;
    }

    public int[] getDomain() {
        return domain;
    }

    public Factor getTable() {
        return table;
    }

    /**
     * Add a child to the node.
     * @param node Child node to add.
     * */
    public void addChild(Node node) {
        if (!children.contains(node)) {
            children.add(node);
        }
    }

    /**
     * Add a parent to the node.
     * @param node Parent node to add.
     * */
    public void addParent(Node node) {
        if (!parents.contains(node)) {
            parents.add(node);
            table.addColumn(node);
        }
    }

    public boolean hasAncestor(Node node) {
        return hasAncestor(node.getLabel());
    }

    /**
     * Depth first search to find if this node has the given node as an ancestor.
     * */
    public boolean hasAncestor(String label) {
        if (parents.size() == 0) {
            return false;
        }
        for (Node parent : parents) {
            if (parent.getLabel().equals(label) || parent.hasAncestor(label)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Node{" + getLabel() + "}";
    }
}
