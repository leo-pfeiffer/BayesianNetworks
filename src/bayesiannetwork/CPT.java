package bayesiannetwork;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

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

    public CPT(Node node) {
        this.node = node;
        this.addColumn(node);
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

    /**
     * Set the CPT values (probabilities).
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
    protected void addColumn(Node node) {
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
            copy.columns.add(column.copy());
        }
        List<Double> newProbabilities = new ArrayList<>(probabilities);
        copy.probabilities.addAll(newProbabilities);
        return copy;
    }

    @Override
    public String toString() {
        // todo test
        if (columns.size() == 0) return "";

        return "Variable: " + this.node.getLabel() + "\n\n" +
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
        sb.append("|\t").append(condProbString());
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
     * Create string for the conditional probability expression
     * e.g. p(A|B)
     * */
    private String condProbString() {
        StringBuilder sb = new StringBuilder();
        sb.append("p(");
        sb.append(this.node.getLabel());
        if (this.columns.size() > 1) {
            sb.append("|");
        }
        for (int i = 0; i < this.columns.size(); i++) {
            String var = this.columns.get(i).getNode().getLabel();
            if (!var.equals(this.node.getLabel())) {
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
