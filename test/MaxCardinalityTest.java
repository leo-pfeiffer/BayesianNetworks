import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import bayesiannetwork.BayesianNetwork;
import bayesiannetwork.BayesianNetworkFactory;
import bayesiannetwork.InducedGraph;
import bayesiannetwork.Node;
import org.junit.Test;

public class MaxCardinalityTest {

    /**
     * Test example from lecture 13 p.27
     * */
    @Test
    public void testExample() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNB();
        MaxCardinality mc = new MaxCardinality();
        Order order = mc.findOrder(bn, bn.getNode("N"));

        // last one must be M
        assertEquals(order.get(order.size()-1).getLabel(), "M");

        // N should not be in there
        assertFalse(order.contains(bn.getNode("N")));
    }

    @Test
    public void testFindMaxCardinalityNodeSingleUnmarked() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNB();

        ArrayList<Node> unmarked = new ArrayList<>();
        ArrayList<Node> marked = new ArrayList<>();

        unmarked.add(bn.getNode("N"));
        marked.add(bn.getNode("M"));

        Node node = MaxCardinality.findMaxCardinalityNode(unmarked, marked, new InducedGraph(bn.getNodes()));
        assertEquals(node.getLabel(), "N");
    }

    @Test
    public void testFindMaxCardinalityNodeAllUnmarked() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNC();

        ArrayList<Node> unmarked = new ArrayList<>(bn.getNodes());
        ArrayList<Node> marked = new ArrayList<>();

        marked.add(bn.getNode("P"));

        Node node = MaxCardinality.findMaxCardinalityNode(unmarked, marked, new InducedGraph(bn.getNodes()));
        assertEquals(node.getLabel(), "Q");
    }

    @Test
    public void testFindMaxCardinalityNodeAllButOneMarked() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNC();

        ArrayList<Node> unmarked = new ArrayList<>();
        ArrayList<Node> marked = new ArrayList<>(bn.getNodes());

        unmarked.add(bn.getNode("P"));
        marked.removeAll(unmarked);

        Node node = MaxCardinality.findMaxCardinalityNode(unmarked, marked, new InducedGraph(bn.getNodes()));
        assertEquals(node.getLabel(), "P");
    }
}
