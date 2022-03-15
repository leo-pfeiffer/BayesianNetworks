package bayesiannetwork;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a column for a variable in the CPT.
 * */
public class CptColumn {

    /**
     * Node represented by this column
     * */
    private final Node node;

    /**
     * Truth values of the column, can take {0, 1}
     * */
    private final List<Integer> truthValues = new ArrayList<>();

    public CptColumn(Node node) {
        this.node = node;
    }

    public List<Integer> getTruthValues() {
        return truthValues;
    }

    /**
     * Sets the truth values of the columns from a list of integers.
     *
     * @param truthValues A list of truth values for the variables in the formula.
     */
    protected void setTruthValues(List<Integer> truthValues) {
        this.truthValues.clear();
        this.truthValues.addAll(truthValues);
    }

    /**
     * Sets the truth values of the columns from an array of integers.
     *
     * @param truthValues An array of truth values for the variables in the formula.
     */
    protected void setTruthValues(int[] truthValues) {
        this.truthValues.clear();
        for (int i : truthValues) {
            this.truthValues.add(i);
        }
    }

    public Node getNode() {
        return node;
    }

    public int getSize() {
        return truthValues.size();
    }

    /**
     * Duplicate the column n times.
     * e.g. if n = 1, [1, 2, 3] -> [1, 2, 3, 1, 2, 3]
     * e.g. if n = 2, [1, 2, 3] -> [1, 2, 3, 1, 2, 3, 1, 2, 3, 1, 2, 3]
     * @param n number of duplications
     * */
    protected void duplicate(int n) {
        List<Integer> copy = new ArrayList<>(truthValues);
        for (int i = 0; i < n; i++) {
            truthValues.addAll(copy);
        }
    }
}
