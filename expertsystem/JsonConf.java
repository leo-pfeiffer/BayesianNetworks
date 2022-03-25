import java.io.Serializable;
import java.util.ArrayList;
import bayesiannetwork.BayesianNetwork;
import bayesiannetwork.Node;

/**
 * Serializable object of program configurations.
 */
public class JsonConf implements Serializable {

    private final String queryNode;
    private final String queryValue;
    private final String evidence;
    private final String order;
    private final String[] providedOrder;
    private String network;

    public JsonConf(
            String queryNode,
            String queryValue,
            String evidence,
            String order,
            String[] providedOrder) {
        this.queryNode = queryNode;
        this.queryValue = queryValue;
        this.evidence = evidence;
        this.order = order;
        this.providedOrder = providedOrder;
    }

    public String getQueryNode() {
        return queryNode;
    }

    public String getQueryValue() {
        return queryValue;
    }

    public String getEvidence() {
        return evidence;
    }

    public String getOrder() {
        return order;
    }

    public String[] getProvidedOrder() {
        return providedOrder;
    }

    public static Agent runConfiguration(JsonConf conf, BayesianNetwork bn) {

        Node node = bn.getNode(conf.getQueryNode());
        System.out.println("Query node: " + node.getLabel());

        int value = Parser.truthValueToInt(conf.getQueryValue());
        System.out.println("Query value: " + value);

        ArrayList<Evidence> evidence = new ArrayList<>();
        if (!conf.getEvidence().equals(""))  {
            evidence = Parser.parseEvidence(conf.getEvidence().split(" "), bn);
        }
        System.out.println("Evidence: " + evidence);

        Order order;
        if (conf.getOrder().equals("Custom")) {
            order = Parser.orderFromInput(conf.getProvidedOrder(), bn);
            System.out.println("Order: " + order);
        } else {
            OrderAlgo orderAlgo = OrderAlgoFactory.create(conf.getOrder());
            order = orderAlgo.findOrder(bn, node);
            System.out.println(conf.getOrder() + " " + order);
        }

        AgentWithEvidence ve = new AgentWithEvidence(bn);

        ve.getResult(node, order, value, evidence);

        return ve;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }
}