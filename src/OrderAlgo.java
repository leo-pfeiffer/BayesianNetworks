import bayesiannetwork.BayesianNetwork;
import bayesiannetwork.Node;

/**
 * Abstract class for order algorithms.
 * */
public abstract class OrderAlgo {


    /**
     * Given a Bayesian network and a query node, find the order of the
     * of elimination.
     *
     * @param bn The Bayesian network to search
     * @param queryNode The node that is being queried.
     * @return The order of elimination.
     */
    abstract Order findOrder(BayesianNetwork bn, Node queryNode);
}
