import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import bayesiannetwork.Node;

public class Order implements Iterable<Node> {
    public final List<Node> order = new ArrayList<>();

    public void add(Node node) {
        order.add(node);
    }

    public void add(int index, Node node) {
        order.add(index, node);
    }

    public void remove(Node node) {
        order.remove(node);
    }

    public void remove(int index) {
        order.remove(index);
    }

    public void removeAll(Collection<Node> nodes) {
        this.order.removeAll(nodes);
    }

    public Node get(int index) {
        return order.get(index);
    }

    public boolean contains(Node node) {
        return order.contains(node);
    }

    public int size() {
        return order.size();
    }

    @Override
    public Iterator<Node> iterator() {
        return this.order.iterator();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order: ");
        for (int i = 0; i < order.size(); i++) {
            sb.append(order.get(i).getLabel());
            if (i < order.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}