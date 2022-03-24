import java.io.Serializable;

public class Experiment implements Serializable {
    String network;
    String queryNode;
    String order;
    String[] providedOrder;
    String evidence;
    long runtime;
    int maxFactorSize;

    public Experiment(
            String network,
            String queryNode,
            String order,
            String[] providedOrder,
            String evidence,
            long runtime,
            int maxFactorSize)
    {
        this.network = network;
        this.queryNode = queryNode;
        this.order = order;
        this.providedOrder = providedOrder;
        this.evidence = evidence;
        this.runtime = runtime;
        this.maxFactorSize = maxFactorSize;
    }
}
