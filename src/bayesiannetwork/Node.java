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
    private final CPT table = new CPT(this);

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

    public CPT getTable() {
        return table;
    }

    /**
     * Add a child to the node.
     * @param node Child node to add.
     * */
    protected void addChild(Node node) {
        if (!children.contains(node)) {
            children.add(node);
        }
    }

    /**
     * Add a parent to the node.
     * @param node Parent node to add.
     * */
    protected void addParent(Node node) {
        if (!parents.contains(node)) {
            parents.add(node);
            table.addColumn(node);
        }
    }

    protected void removeChild(Node node) {
        // need to handle logic to remove the node from the CPT first
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    protected void removeParent(Node node) {
        // need to handle logic to remove the node from the CPT first
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
