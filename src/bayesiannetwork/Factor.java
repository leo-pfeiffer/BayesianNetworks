package bayesiannetwork;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Factor {

    /**
     * Node this factor belongs to.
     * */
    private final Node node;

    /**
     * List of columns of the table.
     * */
    private final List<FactorColumn> columns = new ArrayList<>();

    /**
     * Probabilities of each row.
     */
    private final List<Double> probabilities = new ArrayList<>();

    /**
     * Map to quickly find the row index of a given set of values.
     * */
    private final HashMap<FactorRowKey, Integer> rowIndex = new HashMap<>();

    /**
     * Create table for a conditional probability for the given node.
     * */
    public Factor(Node node) {
        assert node != null;
        this.node = node;
        this.addColumn(node);
    }

    /**
     * Create table for an unconditional probability.
     * */
    public Factor() {
        this.node = null;
    }

    public Node getNode() {
        return node;
    }

    public List<FactorColumn> getColumns() {
        return columns;
    }

    public List<FactorColumn> getColumnsSorted() {
        List<FactorColumn> sortedColumns = new ArrayList<>(this.getColumns());
        sortedColumns.sort(Comparator.comparing(c -> c.getNode().getLabel()));
        return sortedColumns;
    }

    public FactorColumn getColumnByNode(Node node) {
        for (FactorColumn column : this.getColumns()) {
            if (column.getNode() == node) {
                return column;
            }
        }
        return null;
    }

    public List<Double> getProbabilities() {
        return probabilities;
    }

    public HashMap<FactorRowKey, Integer> getRowIndex() {
        return rowIndex;
    }

    public double getProbabilitiesByRowKey(FactorRowKey key) {
        return probabilities.get(rowIndex.get(key));
    }

    public double getProbabilitiesByRowIndex(int rowIndex) {
        return probabilities.get(rowIndex);
    }

    public boolean containsNode(Node node) {
        return containsNode(node.getLabel());
    }

    public boolean containsNode(String label) {
        for (FactorColumn column : columns) {
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
        for (FactorColumn column : columns) {
            if (column.getNode().getLabel().equals(label)) {
                return column.getNode();
            }
        }
        throw new IllegalArgumentException("Node not found in factor.");
    }

    public FactorRowKey getRowKeyForRow(int rowIndex) {
        FactorRowKey key = new FactorRowKey();
        for (FactorColumn column : columns) {
            key.put(column.getNode().getLabel(), column.getTruthValues().get(rowIndex));
        }
        return key;
    }

    /**
     * Set the factor values (probabilities) from a list.
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

    public void setProbabilityForRow(int row, double probability) {
        probabilities.set(row, probability);
    }

    public void setProbabilityForRowKey(FactorRowKey key, double probability) {
        setProbabilityForRow(rowIndex.get(key), probability);
    }

    /**
     * Set the factor values (probabilities) using varargs.
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

            FactorRowKey key = new FactorRowKey(labels, truthValues);
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
        for (FactorColumn column : columns) {
            assert column.getSize() == currentSize;
        }
        return currentSize;
    }

    /**
     * Add a column to the factor.
     * */
    public void addColumn(Node node) {
        FactorColumn newColumn = new FactorColumn(node);

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
            for (FactorColumn column : columns) {
                column.duplicate(domainSize - 1);
            }
        }
        // add the column
        columns.add(newColumn);
    }

    public Factor copy() {
        Factor copy;
        if (this.node == null) {
            copy = new Factor();
        } else {
            copy = new Factor(this.node);
        }
        for (FactorColumn column : columns) {
            if (this.node == null) {
                copy.addColumn(column.getNode());
            }
            else if (!column.getNode().equals(node)) {
                copy.addColumn(column.getNode());
            }
        }
        List<Double> newProbabilities = new ArrayList<>(probabilities);
        copy.setProbabilities(newProbabilities);

        return copy;
    }

    public Set<String> getNodeLabelSet() {
        Set<String> labelSet = new HashSet<>();
        for (FactorColumn col: this.getColumns()) {
            labelSet.add(col.getNode().getLabel());
        }
        return labelSet;
    }

    public Set<Node> getNodeSet() {
        Set<Node> nodeSet = new HashSet<>();
        for (FactorColumn col: this.getColumns()) {
            nodeSet.add(col.getNode());
        }
        return nodeSet;
    }

    @Override
    public String toString() {
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
        for (FactorColumn column : getColumnsSorted()) {
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
            for (FactorColumn column : getColumnsSorted()) {
                sb.append(column.getTruthValues().get(row)).append("\t");
            }

            sb.append("|\t");

            if (probabilities.size() > 0) {
                double p = probabilities.get(row);
                sb.append(dd.format(p));
            } else {
                sb.append("not set");
            }
            sb.append("\n");
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
