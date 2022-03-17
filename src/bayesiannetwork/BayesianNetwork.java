package bayesiannetwork;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents a bayesian network.
 * */
public class BayesianNetwork {

    /**
     * Map from node name to node.
     * */
    private final Map<String, Node> nodeMap = new HashMap<>();

    /**
     * List of all nodes of the network.
     * */
    private final List<Node> nodes = new ArrayList<>();

    public Map<String, Node> getNodeMap() {
        return nodeMap;
    }

    public List<Node> getNodes() {
        return nodes;
    }

    public Node getNode(String label) {
        return nodeMap.get(label);
    }

    /**
     * Create a new node with the given label and add it to the network.
     *
     * @param label The label of the node.
     * @return The node that was added to the graph.
     */
    public Node addNode(String label) {
        Node node = new Node(label);
        this.nodeMap.put(label, node);
        this.nodes.add(node);
        return node;
    }

    /**
     * Add an edge between a parent and child node given their labels if it doesn't already exist
     *
     * @param parent The parent node label.
     * @param child The child node label.
     */
    public void addEdge(String parent, String child) {
        if (!hasNode(parent) || !hasNode(child)) {
            throw new IllegalArgumentException("One of the nodes does not exist");
        }
        Node parentNode = nodeMap.get(parent);
        Node childNode = nodeMap.get(child);
        addEdge(parentNode, childNode);
    }

    /**
     * Add an edge between a parent and child node if it doesn't already exist
     *
     * @param parent The parent node.
     * @param child The child node.
     */
    public void addEdge(Node parent, Node child) {
        if (!hasEdge(parent, child)) {
            parent.addChild(child);
            child.addParent(parent);
        }
    }

    /**
     * Given a parent node and a child node, return true if the child is a child of the parent
     *
     * @param parent The parent node.
     * @param child the child node
     * @return A boolean value.
     */
    public boolean hasEdge(Node parent, Node child) {
        return parent.getChildren().contains(child);
    }

    /**
     * Given a node, return true if the node is in the network
     *
     * @param node The node to check for.
     * @return A boolean value.
     */
    public boolean hasNode(Node node) {
        return hasNode(node.getLabel());
    }

    /**
     * Given a label, return true if a node with the label is in the network
     *
     * @param label The label of the node to be found
     * @return A boolean value.
     */
    public boolean hasNode(String label) {
        return nodeMap.containsKey(label);
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("Nodes: ============================================ \n");
        for (int i = 0; i < nodes.size(); i++) {
            sb.append(nodes.get(i).getLabel());
            if (i < nodes.size() - 1) {
                sb.append(", ");
            } else {
                sb.append("\n");
            }
        }

        sb.append("\n").append("Edges: ============================================ \n");

        for (Node node : nodes) {
            for (Node child : node.getChildren()) {
                sb.append(node.getLabel()).append(" -> ").append(child.getLabel()).append("\n");
            }
        }

        sb.append("\n").append("Factor: ============================================ \n");

        for (Node node : nodes) {
            sb.append(node.getTable()).append("\n");
        }

        return sb.toString();
    }
}
