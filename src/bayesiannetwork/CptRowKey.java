package bayesiannetwork;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class CptRowKey {

    // key: label
    // value: truth value
    private final Map<String, Integer> key = new HashMap<>();

    public CptRowKey(){}

    public CptRowKey(CptRowKey cptRowKey) {
        key.putAll(cptRowKey.key);
    }

    public CptRowKey(List<String> labels, List<Integer> truthValues) {
        if (labels.size() != truthValues.size()) {
            throw new IllegalArgumentException("labels and truthValues must have the same size");
        }
        for (int i = 0; i < labels.size(); i++) {
            put(labels.get(i), truthValues.get(i));
        }
    }

    public CptRowKey(String[] labels, int[] truthValues) {
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
        CptRowKey cptRowKey = (CptRowKey) o;
        return Objects.equals(key, cptRowKey.key);
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
