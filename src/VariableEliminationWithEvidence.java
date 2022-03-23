import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import bayesiannetwork.BayesianNetwork;
import bayesiannetwork.Factor;
import bayesiannetwork.FactorColumn;
import bayesiannetwork.FactorRowKey;
import bayesiannetwork.Node;
import utils.SetUtils;

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
        System.out.println("Pruned " + order);
        List<Factor> factors = createFactorList(order, node);
        projectEvidence(factors, evidence);
        combineFactors(order, factors);
        joinNormalize(factors, node);
        return extract(factors, node, truthValue);
    }

    protected void pruneOrder2(Order order, Node node, ArrayList<Evidence> evidence) {
        List<Node> toRemove = new ArrayList<>();
        for (Node n : order) {

            // is variable ancestor of queried node?
            boolean queryAncestor = node.hasAncestor(n);

            // is variable ancestor of an evidenced node?
            boolean evidenceAncestor = false;
            for (Evidence e : evidence) {

                // retain node if it is an ancestor of an evidence node but not the NODE itself
                if (!e.getNode().equals(node) && (e.getNode().equals(n) || e.getNode().hasAncestor(n))) {
                    if (e.getNode().hasAncestor(node)) {
                        evidenceAncestor = true;
                        break;
                    }
                }
            }

            // remove n if n is neither ancestor of node nor of evidence
            if (!(queryAncestor || evidenceAncestor)) {
                toRemove.add(n);
            }
        }
        order.removeAll(toRemove);
        order.remove(node);
    }

    protected void pruneOrder(Order order, Node node, ArrayList<Evidence> evidence) {
        HashSet<Node> keep = node.getAllAncestors();
        for (Evidence e : evidence) {
            HashSet<Node> evidenceAncestors = e.getNode().getAllAncestors();
            if (SetUtils.intersection(keep, evidenceAncestors).size() > 0 || evidenceAncestors.contains(node)) {
                keep.addAll(evidenceAncestors);
                keep.add(e.getNode());
            }
        }
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
