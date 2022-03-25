import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import bayesiannetwork.BayesianNetwork;
import bayesiannetwork.Factor;
import bayesiannetwork.FactorColumn;
import bayesiannetwork.FactorRowKey;
import bayesiannetwork.Node;
import bayesiannetwork.Tracker;
import utils.SetUtils;

/**
 * Class representing the Variable Elimination algorithm.
 * */
public class Agent {

    private final BayesianNetwork network;

    protected final Tracker tracker;
    protected Double result;

    public Agent(BayesianNetwork network) {
        this.network = network;
        this.tracker = new Tracker();
    }

    public BayesianNetwork getNetwork() {
        return network;
    }

    /**
     * Run the variable elimination algorithm.
     * */
    public Double getResult(Node node, Order order, int truthValue) {
        tracker.startTracker();
        pruneOrder(order, node);

        List<Factor> factors = createFactorList(order, node);
        tracker.trackMaxFactorSize(factors);

        combineFactors(order, factors);
        tracker.trackMaxFactorSize(factors);

        Double result = extract(factors, node, truthValue);
        tracker.stopTracker();
        this.result = result;
        return result;

    }

    protected void combineFactors(Order order, List<Factor> factors) {
        for (Node n : order) {
            String y = n.getLabel();
            List<Factor> toSumOut = createToSumOutList(factors, y);
            factors.removeAll(toSumOut);
            Factor newFactor = joinMarginalize(toSumOut, y);
            factors.add(newFactor);
        }
    }

    /**
     * Prune all nodes from the order that are not ancestors of the given node.
     * */
    private void pruneOrder(Order order, Node node) {
        List<Node> toRemove = new ArrayList<>();
        for (Node n : order) {
            if (!node.hasAncestor(n)) {
                toRemove.add(n);
            }
        }
        order.removeAll(toRemove);
    }

    /**
     * Create a list containing copies of the factors of all nodes of the order.
     * */
    protected List<Factor> createFactorList(Order order, Node node) {
        List<Factor> factors = new ArrayList<>();
        for (Node n : this.network.getNodes()) {
            if (order.contains(n) || n.equals(node)) {
                factors.add(n.getTable().copy());
            }
        }
        return factors;
    }

    /**
     * Create the ToSumOut list containing all factors that contain the variable with the label.
     * */
    private List<Factor> createToSumOutList(List<Factor> factors, String label) {
        List<Factor> toSumOut = new ArrayList<>();

        for (Factor f : factors) {
            if (f.containsNode(label)) {
                toSumOut.add(f);
            }
        }
        return toSumOut;
    }

    /**
     * Create new factor with all variables in factors of toSumOut but without Y by eliminating Y.
     * */
    protected Factor joinMarginalize(List<Factor> toSumOut, String y) {
        Factor fResult = join(toSumOut);
        return marginalize(fResult, y);
    }

    /**
     * Join factors of toSumOut using the point wise product.
     * */
    protected Factor join(List<Factor> toSumOut) {
        Factor result = toSumOut.get(0);
        for (int i = 1; i < toSumOut.size(); i++) {
            result = pointWiseProduct(result, toSumOut.get(i));
        }
        return result;
    }

    /**
     * Compute the point wise product of two factors.
     * */
    protected Factor pointWiseProduct(Factor f1, Factor f2) {

        // variables both in f1 and f2
        Set<Node> v1 = SetUtils.intersection(f1.getNodeSet(), f2.getNodeSet());

        // variables in f1 but not in f2
        Set<Node> v2 = new HashSet<>(f1.getNodeSet());
        v2.removeAll(f2.getNodeSet());

        // variables in f2 but not in f1
        Set<Node> v3 = new HashSet<>(f2.getNodeSet());
        v3.removeAll(f1.getNodeSet());

        // create the raw resulting table
        Factor result = new Factor();
        for (Node n : v1) result.addColumn(n);
        for (Node n : v2) result.addColumn(n);
        for (Node n : v3) result.addColumn(n);

        Set<Node> leftSide = SetUtils.union(v1, v2);
        Set<Node> rightSide = SetUtils.union(v1, v3);

        // list to be filled with the truth values of the new table
        List<Double> truthValues = new ArrayList<>(result.getNumRows());

        // row by row over the new table
        for (int row = 0; row < result.getNumRows(); row++) {

            // keys to access the truth values of f1 and f2
            FactorRowKey leftKey = new FactorRowKey();
            FactorRowKey rightKey = new FactorRowKey();

            for (FactorColumn c : result.getColumns()) {
                Node n = c.getNode();

                // if the node is on the left side of the product
                if (leftSide.contains(n)) {
                    leftKey.put(n.getLabel(), c.getTruthValues().get(row));
                }
                // ... or on the right side of the product
                if (rightSide.contains(n)) {
                    rightKey.put(n.getLabel(), c.getTruthValues().get(row));
                }
            }

            // get the truth value of the row
            double truthValue = f1.getProbabilitiesByRowKey(leftKey) * f2.getProbabilitiesByRowKey(rightKey);
            truthValues.add(truthValue);
        }

        // fill the table with the truth values
        result.setProbabilities(truthValues);

        return result;
    }

    /**
     * Marginalize the factor f by summing out variable y.
     * */
    protected Factor marginalize(Factor f, String y) {

        // map the new row keys to the new truth values
        HashMap<FactorRowKey, Double> newTruthValues = this.createTruthValuesForMarginalization(f, y);

        // create the new table
        Factor result = this.marginalizeCreateTable(f, y);

        // put the truth values in the right order
        List<Double> truthValues = this.orderTruthValuesForFactor(result, newTruthValues);

        // add the truth values to the new table
        result.setProbabilities(truthValues);

        return result;

    }

    /**
     * Given the factor f and the variable y to marginalize,
     * group by the row keys of f (without column y) and sum the probabilities.
     * */
    private HashMap<FactorRowKey, Double> createTruthValuesForMarginalization(Factor f, String y) {

        Node yNode = f.getNodeFromColumns(y);

        // rows already explored to prevent double counting
        HashSet<FactorRowKey> explored = new HashSet<>(f.getNumRows());

        // map the new row keys to the new truth values
        HashMap<FactorRowKey, Double> newTruthValues = new HashMap<>(f.getNumRows()/2);

        // sum out the variable y
        for (int row = 0; row < f.getNumRows(); row++) {

            FactorRowKey rowKey = f.getRowKeyForRow(row);
            // already explored this row
            if (explored.contains(rowKey)) continue;

            // create a key for every domain value of y
            ArrayList<FactorRowKey> keys = new ArrayList<>(yNode.getDomain().length);
            for (int d : yNode.getDomain()) {
                FactorRowKey key = new FactorRowKey(rowKey);
                key.put(y, d);
                keys.add(key);
            }

            // compute combined truth value of the keys
            double truthValue = 0;
            for (FactorRowKey key : keys) {
                truthValue += f.getProbabilitiesByRowKey(key);
            }

            // add the truth value to the new table
            FactorRowKey newRowKey = new FactorRowKey(rowKey);
            newRowKey.remove(y);
            newTruthValues.put(newRowKey, truthValue);

            // add to explored
            explored.addAll(keys);
        }

        return newTruthValues;
    }

    /**
     * Create the new Factor table for the marginalization.
     * This simply creates a new factor from the old factor's columns minus the variable y.
     * */
    private Factor marginalizeCreateTable(Factor f, String y) {
        Factor result = new Factor();
        for (FactorColumn c : f.getColumns()) {
            if (!c.getNode().getLabel().equals(y)) {
                result.addColumn(c.getNode());
            }
        }
        return result;
    }

    /**
     * Given a factor and a map of row keys to truth values, create a list that contains the truth values
     * in the order in which they appear in the table.
     */
    private List<Double> orderTruthValuesForFactor(Factor factor, HashMap<FactorRowKey, Double> newTruthValues) {
        List<Double> truthValues = new ArrayList<>(factor.getNumRows());
        for (int row = 0; row < factor.getNumRows(); row++) {
            FactorRowKey key = factor.getRowKeyForRow(row);
            truthValues.add(newTruthValues.get(key));
        }
        return truthValues;
    }

    /**
     * Extract the result from factors = [f(node)], which is f(node) using the truth value.
     * */
    protected Double extract(List<Factor> factors, Node node, int truthValue) {
        Factor f = factors.get(0);
        FactorRowKey key = new FactorRowKey();
        key.put(node.getLabel(), truthValue);
        return f.getProbabilitiesByRowKey(key);
    }
}
