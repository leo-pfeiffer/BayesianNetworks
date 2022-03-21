import bayesiannetwork.BayesianNetwork;
import bayesiannetwork.Node;

public abstract class OrderAlgo {

    abstract Order findOrder(BayesianNetwork bn, Node queryNode);
}
