import bayesiannetwork.Node;

public class Evidence {
    private final Node node;
    private final int value;

    public Evidence(Node node, int value) {
        this.node = node;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    public Node getNode() {
        return node;
    }

    @Override
    public String toString() {
        return node.getLabel() + "=" + value;
    }
}
