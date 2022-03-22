import java.util.ArrayList;
import bayesiannetwork.BayesianNetwork;
import bayesiannetwork.Node;

public class Parser {

    public static int truthValueToInt(String value) {
        switch (value) {
            case "T":
                return 1;
            case "F":
                return 0;
            default:
                throw new IllegalArgumentException("Invalid value: " + value);
        }
    }

    public static Order orderFromInput(String[] orderRaw, BayesianNetwork bn) {
        Order order = new Order();
        for (String n : orderRaw) {
            Node node = bn.getNode(n);
            if (node == null) {
                throw new IllegalArgumentException("Invalid node: " + n);
            }
            order.add(node);
        }
        return order;
    }

    public static Node getNode(String variable, BayesianNetwork bn) {
        Node variableNode = bn.getNode(variable);
        if (variableNode == null) {
            throw new IllegalArgumentException("Invalid node: " + variable);
        }
        return variableNode;
    }

    public static ArrayList<Evidence> parseEvidence(String[] evidenceRaw, BayesianNetwork bn) {
        ArrayList<Evidence> evidence = new ArrayList<>();
        for (String st : evidenceRaw) {
            String[] ev = st.split(":");
            int truthValue = Parser.truthValueToInt(ev[1]);
            Node node = Parser.getNode(ev[0], bn);
            evidence.add(new Evidence(node, truthValue));
        }
        return evidence;
    }

}
