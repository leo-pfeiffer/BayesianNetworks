import java.util.ArrayList;
import java.util.List;
import bayesiannetwork.BayesianNetwork;
import bayesiannetwork.CPT;
import bayesiannetwork.Node;

/**
 * Class representing the Variable Elimination algorithm.
 * */
public class VariableElimination {

    private final BayesianNetwork network;
    private final Node node;
    private final Order order;
    private final int truthValue;

    public VariableElimination(BayesianNetwork network, Node node, Order order, int truthValue) {
        this.network = network;
        this.node = node;
        this.order = order;
        this.truthValue = truthValue;
    }

    public BayesianNetwork getNetwork() {
        return network;
    }

    public Node getNode() {
        return node;
    }

    public Order getOrder() {
        return order;
    }

    public int getTruthValue() {
        return truthValue;
    }

    /**
     * Run the variable elimination algorithm.
     * */
    public Double getResult() {
        pruneOrder();
        List<CPT> factors = createFactorList();

        for (Node n : this.order) {
            String y = n.getLabel();
            List<CPT> toSumOut = createToSumOutList(factors, y);
            factors.removeAll(toSumOut);
            CPT newFactor = joinMarginalise(toSumOut, y);
            factors.add(newFactor);
        }

        return extract(factors);

    }

    /**
     * Prune all nodes from the order that are not ancestors of the given node.
     * */
    private void pruneOrder() {
        List<Node> toRemove = new ArrayList<>();
        for (Node n : this.order) {
            if (!this.node.hasAncestor(n)) {
                toRemove.add(n);
            }
        }
        this.order.removeAll(toRemove);
    }

    /**
     * Create a list containing copies of the factors of all nodes of the network.
     * */
    private List<CPT> createFactorList() {
        List<CPT> factors = new ArrayList<>();
        for (Node n : this.network.getNodes()) {
            factors.add(n.getTable().copy());
        }
        return factors;
    }

    /**
     * Create the ToSumOut list containing all factors that contain the variable with the label.
     * */
    private List<CPT> createToSumOutList(List<CPT> factors, String label) {
        List<CPT> toSumOut = new ArrayList<>();

        for (CPT f : factors) {
            if (f.containsNode(label)) {
                toSumOut.add(f);
            }
        }
        return toSumOut;
    }

    /**
     * Create new factor with all variables in factors of toSumOut but without Y by eliminating Y.
     * */
    private CPT joinMarginalise(List<CPT> toSumOut, String label) {
        // todo
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    /**
     * Extract the result from factors = [f(node)], which is f(node) using the truth value.
     * */
    private Double extract(List<CPT> factors) {
        // todo
        throw new UnsupportedOperationException("Not implemented yet.");
    }
}
