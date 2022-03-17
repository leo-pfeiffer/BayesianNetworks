package bayesiannetwork;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class CPT {

    /**
     * Node this factor belongs to.
     * */
    private final Node node;

    /**
     * List of columns of the table.
     * */
    private final List<CptColumn> columns = new ArrayList<>();

    /**
     * Probabilities of each row.
     */
    private final List<Double> probabilities = new ArrayList<>();

    /**
     * Map to quickly find the row index of a given set of values.
     * */
    private final HashMap<CptRowKey, Integer> rowIndex = new HashMap<>();

    /**
     * Create table for a conditional probability for the given node.
     * */
    public CPT(Node node) {
        this.node = node;
        this.addColumn(node);
    }

    /**
     * Create table for an unconditional probability.
     * */
    public CPT() {
        this.node = null;
    }

    public Node getNode() {
        return node;
    }

    public List<CptColumn> getColumns() {
        return columns;
    }

    public List<Double> getProbabilities() {
        return probabilities;
    }

    public HashMap<CptRowKey, Integer> getRowIndex() {
        return rowIndex;
    }

    public double getProbabilitiesByRowKey(CptRowKey key) {
        return probabilities.get(rowIndex.get(key));
    }

    public double getProbabilitiesByRowIndex(int rowIndex) {
        return probabilities.get(rowIndex);
    }

    public boolean containsNode(Node node) {
        return containsNode(node.getLabel());
    }

    public boolean containsNode(String label) {
        for (CptColumn column : columns) {
            if (column.getNode().getLabel().equals(label)) {
                return true;
            }
        }
        return false;
    }

    public Node getNodeFromColumns(Node node) {
        return getNodeFromColumns(node.getLabel());
    }

    public Node getNodeFromColumns(String label) {
        for (CptColumn column : columns) {
            if (column.getNode().getLabel().equals(label)) {
                return column.getNode();
            }
        }
        throw new IllegalArgumentException("Node not found in CPT.");
    }

    public CptRowKey getRowKeyForRow(int rowIndex) {
        CptRowKey key = new CptRowKey();
        for (CptColumn column : columns) {
            key.put(column.getNode().getLabel(), column.getTruthValues().get(rowIndex));
        }
        return key;
    }

    /**
     * Set the CPT values (probabilities) from a list.
     * @param probabilities Probabilities. Must be the same size as the number of rows.
     * */
    public void setProbabilities(List<Double> probabilities) {
        if (probabilities.size() != this.getNumRows()) {
            throw new IllegalArgumentException("Number of probabilities does not match number of rows.");
        }
        this.probabilities.clear();
        this.probabilities.addAll(probabilities);
        createRowIndex();
    }

    /**
     * Set the CPT values (probabilities) using varargs.
     * @param probabilities Probabilities. Must be the same size as the number of rows.
     * */
    public void setProbabilities(double ...probabilities) {
        if (probabilities.length != this.getNumRows()) {
            throw new IllegalArgumentException("Number of probabilities does not match number of rows.");
        }
        this.probabilities.clear();
        for (double p : probabilities) {
            this.probabilities.add(p);
        }
        createRowIndex();
    }

    public void createRowIndex() {
        rowIndex.clear();

        String[] labels = new String[columns.size()];
        for (int i = 0; i < columns.size(); i++) {
            labels[i] = columns.get(i).getNode().getLabel();
        }

        for (int i = 0; i < getNumRows(); i++) {
            int[] truthValues = new int[columns.size()];
            for (int j = 0; j < columns.size(); j++) {
                truthValues[j] = columns.get(j).getTruthValues().get(i);
            }

            CptRowKey key = new CptRowKey(labels, truthValues);
            rowIndex.put(key, i);
        }
    }

    /**
     * Return the number of rows in the table.
     * */
    public int getNumRows() {
        if (columns.size() == 0) {
            return 0;
        }
        // sanity check: all columns should have the same size
        int currentSize = columns.get(0).getSize();
        for (CptColumn column : columns) {
            assert column.getSize() == currentSize;
        }
        return currentSize;
    }

    /**
     * Add a column to the CPT.
     * */
    public void addColumn(Node node) {
        CptColumn newColumn = new CptColumn(node);

        if (columns.size() == 0) {
            newColumn.setTruthValues(node.getDomain());
        } else {
            int domainSize = node.getDomain().length;

            // sanity check: all columns should have the same size
            int currentSize = this.getNumRows();

            // create new values
            List<Integer> newTruthValues = new ArrayList<>(currentSize*domainSize);

            // add each value of the domain exactly "currentSize" times
            for (int d : node.getDomain()) {
                for (int i = 0; i < currentSize; i++) {
                    newTruthValues.add(d);
                }
            }

            newColumn.setTruthValues(newTruthValues);

            // increase size of table to make room for new column
            for (CptColumn column : columns) {
                column.duplicate(domainSize - 1);
            }
        }
        // add the column
        columns.add(newColumn);
    }

    public CPT copy() {
        CPT copy = new CPT(this.node);
        for (CptColumn column : columns) {
            if (!column.getNode().equals(node)) {
                copy.addColumn(column.getNode());
            }
        }
        List<Double> newProbabilities = new ArrayList<>(probabilities);
        copy.setProbabilities(newProbabilities);

        return copy;
    }

    public Set<String> getNodeLabelSet() {
        Set<String> labelSet = new HashSet<>();
        for (CptColumn col: this.getColumns()) {
            labelSet.add(col.getNode().getLabel());
        }
        return labelSet;
    }

    public Set<Node> getNodeSet() {
        Set<Node> nodeSet = new HashSet<>();
        for (CptColumn col: this.getColumns()) {
            nodeSet.add(col.getNode());
        }
        return nodeSet;
    }

    @Override
    public String toString() {
        // todo test
        if (columns.size() == 0) return "";

        String firstLine = "";
        if (this.node != null) {
            firstLine = "Variable: " + this.node.getLabel() + "\n\n";
        }

        return firstLine +
                tableHeaderString() + "\n" +
                innerTableString();
    }

    /**
     * Create string for the table header.
     * e.g.: A	B	|	p(A|B)
     * */
    private String tableHeaderString() {
        StringBuilder sb = new StringBuilder();
        for (CptColumn column : columns) {
            String v = column.getNode().getLabel();
            sb.append(v).append("\t");
        }
        sb.append("|\t").append(probabilityExpression());
        return sb.toString();
    }

    /**
     * Create string for the table body.
     * e.g.:
     * 0	0	|	0.10000
     * 1	0	|	0.90000
     * 0	1	|	0.20000
     * 1	1	|	0.80000
     * */
    private String innerTableString() {
        DecimalFormat dd = new DecimalFormat("#0.00000");
        StringBuilder sb = new StringBuilder();
        for (int row = 0; row < this.getNumRows(); row++) {
            for (CptColumn column : columns) {
                sb.append(column.getTruthValues().get(row)).append("\t");
            }

            double p = probabilities.get(row);
            sb.append("|\t").append(dd.format(p)).append("\n");
        }
        return sb.toString();
    }

    /**
     * Create string for the probability expression
     * e.g. p(A|B) or P(A,B)
     * */
    private String probabilityExpression() {
        StringBuilder sb = new StringBuilder();
        sb.append("p(");
        if (this.node != null) {
            sb.append(this.node.getLabel());
            if (this.columns.size() > 1) {
                sb.append("|");
            }
        }
        for (int i = 0; i < this.columns.size(); i++) {
            String var = this.columns.get(i).getNode().getLabel();

            // unconditional probability
            if (this.node == null) {
                sb.append(var);
                if (i != this.columns.size() - 1) {
                    sb.append(",");
                }
            }
            // conditional probability
            else if (!var.equals(this.node.getLabel())) {
                sb.append(var);
                if (i != this.columns.size() - 1) {
                    sb.append(",");
                }
            }
        }
        sb.append(")");
        return sb.toString();
    }
}
