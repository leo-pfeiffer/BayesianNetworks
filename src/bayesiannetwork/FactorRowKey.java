package bayesiannetwork;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FactorRowKey {

    // key: label
    // value: truth value
    private final Map<String, Integer> key = new HashMap<>();

    public FactorRowKey(){}

    public FactorRowKey(FactorRowKey factorRowKey) {
        key.putAll(factorRowKey.key);
    }

    public FactorRowKey(List<String> labels, List<Integer> truthValues) {
        if (labels.size() != truthValues.size()) {
            throw new IllegalArgumentException("labels and truthValues must have the same size");
        }
        for (int i = 0; i < labels.size(); i++) {
            put(labels.get(i), truthValues.get(i));
        }
    }

    public FactorRowKey(String[] labels, int[] truthValues) {
        if (labels.length != truthValues.length) {
            throw new IllegalArgumentException("labels and truthValues must have the same size");
        }
        for (int i = 0; i < labels.length; i++) {
            put(labels[i], truthValues[i]);
        }
    }

    public void put(String label, int truthValue) {
        key.put(label, truthValue);
    }

    public void remove(String label) {
        key.remove(label);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FactorRowKey factorRowKey = (FactorRowKey) o;
        return Objects.equals(key, factorRowKey.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        for (Map.Entry<String, Integer> entry : key.entrySet()) {
            sb.append(entry.getKey()).append("=").append(entry.getValue()).append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("}");
        return sb.toString();
    }
}
