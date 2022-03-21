import java.util.ArrayList;
import bayesiannetwork.BayesianNetwork;
import bayesiannetwork.InducedGraph;
import bayesiannetwork.Node;

public class MaxCardinality extends OrderAlgo{

    @Override
    public Order findOrder(BayesianNetwork bn, Node queryNode) {
        InducedGraph g = new InducedGraph(bn.getNodes());
        ArrayList<Node> unmarked = new ArrayList<>(bn.getNodes());
        ArrayList<Node> marked = new ArrayList<>();
        ArrayList<Node> order = new ArrayList<>();

        // mark the queried node
        order.add(queryNode);
        unmarked.remove(queryNode);
        marked.add(queryNode);

        for (int i = 1; i < bn.getNodes().size(); i++) {
            Node maxCardinalityNode = findMaxCardinalityNode(unmarked, marked, g);
            order.add(maxCardinalityNode);
            unmarked.remove(maxCardinalityNode);
            marked.add(maxCardinalityNode);
        }

        Order finalOrder = new Order();
        // create Order data structure in (reversed)
        for (int i = order.size() - 1; i >= 0; i--) {
            finalOrder.add(order.get(i));
        }

        finalOrder.remove(queryNode);
        return finalOrder;
    }

    protected static Node findMaxCardinalityNode(ArrayList<Node> unmarked, ArrayList<Node> marked, InducedGraph g) {
        Node maxCardinalityNode = null;
        int maxCardinality = 0;

        for (Node node : unmarked) {
            int currCardinality = 0;

            for (Node nbr : g.getGraph().get(node)) {
                if (marked.contains(nbr)) {
                    currCardinality++;
                }
            }

            if (currCardinality > maxCardinality) {
                maxCardinality = currCardinality;
                maxCardinalityNode = node;
            }
        }

        return maxCardinalityNode;
    }
}
