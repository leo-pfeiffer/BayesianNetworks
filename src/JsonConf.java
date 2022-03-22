import java.io.Serializable;

public class JsonConf implements Serializable {

    private final String queryNode;
    private final String queryValue;
    private final String evidence;
    private final String order;
    private final String providedOrder;

    public JsonConf(
            String queryNode,
            String queryValue,
            String evidence,
            String order,
            String providedOrder) {
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

    public String getProvidedOrder() {
        return providedOrder;
    }
}