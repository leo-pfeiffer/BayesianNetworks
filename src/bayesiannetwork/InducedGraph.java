package bayesiannetwork;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InducedGraph {

    private final Map<Node,List<Node>> graph;

    public InducedGraph(List<Node> nodes) {
        this.graph = new HashMap<>();

        for (Node node : nodes) {
            // edges between parents
            for (Node parentNode : node.getParents()) {
                for (Node otherParentNode : node.getParents()) {
                    if (!parentNode.equals(otherParentNode)) {
                        createEdge(parentNode, otherParentNode);
                    }
                }
            }
            // edges between node and children
            for (Node childNode : node.getChildren()) {
                createEdge(node, childNode);
            }
        }
    }

    public Map<Node, List<Node>> getGraph() {
        return graph;
    }

    public List<Node> getNodes() {
        return new ArrayList<>(graph.keySet());
    }

    private void createEdge(Node node1, Node node2) {
        addNodeToOtherNode(node1, node2);
        addNodeToOtherNode(node2, node1);
    }

    /**
     * Add node2 to list of nodes of node1
     * */
    private void addNodeToOtherNode(Node node1, Node node2) {
        if (graph.containsKey(node1)) {
            if (!graph.get(node1).contains(node2)) {
                graph.get(node1).add(node2);
            }
        }
        else {
            List<Node> nodes = new java.util.ArrayList<>();
            nodes.add(node2);
            graph.put(node1, nodes);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        // sort alphabetically
        List<Node> nodes = new ArrayList<>(graph.keySet());
        nodes.sort(Comparator.comparing(Node::getLabel));
        for (Node node : nodes) {
            sb.append(node.getLabel()).append(" -> ");
            for (int i = 0; i < graph.get(node).size(); i++) {
                sb.append(graph.get(node).get(i).getLabel());
                if (i < graph.get(node).size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        }

        return sb.toString();
    }
}
