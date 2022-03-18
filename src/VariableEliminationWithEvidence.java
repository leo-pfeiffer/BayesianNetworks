import java.util.ArrayList;
import java.util.List;
import bayesiannetwork.BayesianNetwork;
import bayesiannetwork.Factor;
import bayesiannetwork.FactorColumn;
import bayesiannetwork.FactorRowKey;
import bayesiannetwork.Node;

public class VariableEliminationWithEvidence extends VariableElimination {

    public VariableEliminationWithEvidence(BayesianNetwork network) {
        super(network);
    }

    @Override
    public Double getResult(Node node, Order order, int truthValue) {
        throw new UnsupportedOperationException("Please provide evidence.");
    }

    public Double getResult(Node node, Order order, int truthValue, ArrayList<Evidence> evidence) {
        pruneOrder(order, node, evidence);
        List<Factor> factors = createFactorList(order, node);
        projectEvidence(factors, evidence);
        combineFactors(order, factors);
        joinNormalise(factors, node);
        return extract(factors, node, truthValue);
    }

    protected void pruneOrder(Order order, Node node, ArrayList<Evidence> evidence) {
        List<Node> toRemove = new ArrayList<>();
        for (Node n : order) {

            // check if node is ancestor of evidence node
            boolean evidenceAncestor = false;
            for (Evidence e : evidence) {
                if (e.getNode().equals(n) || e.getNode().hasAncestor(n)) {
                    evidenceAncestor = true;
                    break;
                }
            }

            // remove n if n is neither ancestor of node nor of evidence
            if (!node.hasAncestor(n) && !evidenceAncestor) {
                toRemove.add(n);
            }
        }
        order.removeAll(toRemove);
    }

    /**
     * Project the evidence onto the factors.
     * */
    protected void projectEvidence(List<Factor> factors, ArrayList<Evidence> evidence) {
        for (Evidence e : evidence) {

            // find factor of evidence
            Factor evFactor = null;
            for (Factor f : factors) {
                if (f.getNode().equals(e.getNode())) {
                    evFactor = f;
                    break;
                }
            }
            if (evFactor == null) throw new AssertionError("This shouldn't happen.");

            // set the probabilities for the evidence
            FactorColumn evCol = evFactor.getColumnByNode(e.getNode());
            for (int row = 0; row < evFactor.getNumRows(); row++) {
                if (evCol.getTruthValues().get(row) == e.getValue()) {
                    evFactor.setProbabilityForRow(row, 0);
                }
            }
        }
    }

    protected void joinNormalise(List<Factor> factors, Node node) {
        // sanity check: make sure all factors are of the correct node
        for (Factor f : factors) {
            if (f.getColumns().size() > 1) {
                throw new AssertionError("All factors should only have one column");
            }
            if (f.getColumns().get(0).getNode() != node) {
                throw new AssertionError("All columns should contain node");
            }
        }
        // create new factor from first factor in the list
        Factor newFactor = factors.get(0).copy();
        ArrayList<FactorRowKey> keys = new ArrayList<>();
        for (int row = 0; row < newFactor.getNumRows(); row++) {
            FactorRowKey key = new FactorRowKey();
            key.put(node.getLabel(), newFactor.getColumnByNode(node).getTruthValues().get(row));
            keys.add(key);
        }

        // join other factors
        for (int i = 1; i < factors.size(); i++) {
            Factor f = factors.get(i);
            for (FactorRowKey key : keys) {
                double p1 = newFactor.getProbabilitiesByRowKey(key);
                double p2 = f.getProbabilitiesByRowKey(key);
                newFactor.setProbabilityForRowKey(key, p1 * p2);
            }
        }

        // replace previous factors with new factor
        factors.clear();
        factors.add(newFactor);
    }
}
