import java.util.ArrayList;
import bayesiannetwork.BayesianNetwork;
import bayesiannetwork.InducedGraph;
import bayesiannetwork.Node;

/**
 * GreedyMinEdges is an implementation of the greedy minimum edges algorithm.
 */
public class GreedyMinEdges extends OrderAlgo {

    @Override
    public Order findOrder(BayesianNetwork bn, Node queryNode) {
        InducedGraph g = new InducedGraph(bn.getNodes());
        ArrayList<Node> unmarked = new ArrayList<>(bn.getNodes());
        ArrayList<Node> marked = new ArrayList<>();
        ArrayList<Node> order = new ArrayList<>();

        for (int i = 0; i < bn.getNodes().size(); i++) {
            Node minNeighborNode = findMinNeighborsNode(unmarked, g);
            order.add(minNeighborNode);
            unmarked.remove(minNeighborNode);
            marked.add(minNeighborNode);
            addEdgesForPairs(minNeighborNode, g);
        }
        order.remove(queryNode);
        Order finalOrder = new Order();
        for (Node n : order) {
            finalOrder.add(n);
        }
        return finalOrder;
    }

    protected static Node findMinNeighborsNode(ArrayList<Node> unmarked, InducedGraph g) {
        Node minNeighborNode = null;
        int minNeighbors = Integer.MAX_VALUE;
        for (Node node : unmarked) {
            int neighbors = g.getGraph().get(node).size();
            if (neighbors < minNeighbors) {
                minNeighborNode = node;
                minNeighbors = neighbors;
            }
        }
        return minNeighborNode;
    }

    protected static void addEdgesForPairs(Node node, InducedGraph g) {
        for (Node nbr1 : g.getGraph().get(node)) {
            for (Node nbr2 : g.getGraph().get(node)) {
                if (nbr1 != nbr2) {
                    g.createEdge(nbr1, nbr2);
                }
            }
        }
    }

}
