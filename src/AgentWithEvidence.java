import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import bayesiannetwork.BayesianNetwork;
import bayesiannetwork.Factor;
import bayesiannetwork.FactorColumn;
import bayesiannetwork.FactorRowKey;
import bayesiannetwork.Node;
import utils.SetUtils;

/**
 * The AgentWithEvidence class is an agent that can be used to query a
 * Bayesian network with evidence.
 */
public class AgentWithEvidence extends Agent {

    public AgentWithEvidence(BayesianNetwork network) {
        super(network);
    }

    @Override
    public Double getResult(Node node, Order order, int truthValue) {
        throw new UnsupportedOperationException("Please provide evidence.");
    }

    public Double getResult(Node node, Order order, int truthValue, ArrayList<Evidence> evidence) {
        tracker.startTracker();
        pruneOrder(order, node, evidence);

        List<Factor> factors = createFactorList(order, node);
        tracker.trackMaxFactorSize(factors);

        projectEvidence(factors, evidence);
        combineFactors(order, factors);
        tracker.trackMaxFactorSize(factors);

        joinNormalize(factors, node);
        tracker.trackMaxFactorSize(factors);

        Double result = extract(factors, node, truthValue);
        tracker.stopTracker();
        this.result = result;
        return result;
    }

    /**
     * General order pruning algorithm.
     * */
    protected void pruneOrder(Order order, Node node, ArrayList<Evidence> evidence) {
        // set of nodes to keep in the order
        HashSet<Node> keep = node.getAllAncestors();

        for (Evidence e : evidence) {
            HashSet<Node> evidenceAncestors = e.getNode().getAllAncestors();

            // if the evidence node and the query node have ancestors in common, keep the evidence node ancestors
            if (SetUtils.intersection(keep, evidenceAncestors).size() > 0 || evidenceAncestors.contains(node)) {
                keep.addAll(evidenceAncestors);
                keep.add(e.getNode());
            }
        }

        // remove all nodes not in keep (preserving the order)
        HashSet<Node> toRemove = new HashSet<>();
        for (Node n : order) {
            if (!keep.contains(n)) {
                toRemove.add(n);
            }
        }

        order.removeAll(toRemove);
        order.remove(node);
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
             if (evFactor == null) {
                 continue;
                 // throw new AssertionError("This shouldn't happen.");
             }

            // set the probabilities for the evidence
            FactorColumn evCol = evFactor.getColumnByNode(e.getNode());
            for (int row = 0; row < evFactor.getNumRows(); row++) {
                if (evCol.getTruthValues().get(row) != e.getValue()) {
                    evFactor.setProbabilityForRow(row, 0);
                }
            }
        }
    }

    protected void joinNormalize(List<Factor> factors, Node node) {
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

        // sanity check: factor should only contain two rows
        if (newFactor.getNumRows() != 2) throw new AssertionError("Factor should only contain two rows");

        // normalize
        double colSum = 0;
        for (double p : newFactor.getProbabilities()) colSum += p;
        for (int row = 0; row < newFactor.getNumRows(); row++) {
            double newProb = newFactor.getProbabilities().get(row) / colSum;
            newFactor.setProbabilityForRow(row, newProb);
        }

        // sanity check: probabilities should sum to 1
        double sum = 0;
        for (double p : newFactor.getProbabilities()) sum += p;
        if (Math.abs(sum - 1) > 1e-6) throw new AssertionError("Probabilities should sum to 1");

        // replace previous factors with new factor
        factors.clear();
        factors.add(newFactor);
    }
}
