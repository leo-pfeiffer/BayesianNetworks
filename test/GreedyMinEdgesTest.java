import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import bayesiannetwork.BayesianNetwork;
import bayesiannetwork.BayesianNetworkFactory;
import bayesiannetwork.InducedGraph;
import bayesiannetwork.Node;
import org.junit.Test;

public class GreedyMinEdgesTest {

    /**
     * Test example from lecture 13 p.34
     * */
    @Test
    public void testExample() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNB();
        GreedyMinEdges gme = new GreedyMinEdges();
        Order order = gme.findOrder(bn, bn.getNode("N"));

        // first two must be O or J
        String first= order.get(0).getLabel();
        String second= order.get(1).getLabel();
        assertNotEquals(first, second);
        assertTrue(first.equals("O") || first.equals("J"));
        assertTrue(second.equals("O") || second.equals("J"));

        // next two must be J, then K, then M
        assertEquals(order.get(2).getLabel(), "L");
        assertEquals(order.get(3).getLabel(), "K");
        assertEquals(order.get(4).getLabel(), "M");

        assertEquals(order.size(), 5);

        // N should not be in there
        assertFalse(order.contains(bn.getNode("N")));
    }

    @Test
    public void testFindMinNeighborsNodeSingleUnmarked() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNB();

        ArrayList<Node> unmarked = new ArrayList<>();

        unmarked.add(bn.getNode("N"));

        Node node = GreedyMinEdges.findMinNeighborsNode(unmarked, new InducedGraph(bn.getNodes()));
        assertEquals(node.getLabel(), "N");
    }

    @Test
    public void testFindMinNeighborsNodeAllUnmarked() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNB();

        ArrayList<Node> unmarked = new ArrayList<>(bn.getNodes());

        Node node = GreedyMinEdges.findMinNeighborsNode(unmarked, new InducedGraph(bn.getNodes()));
        String label = node.getLabel();
        assertTrue(label.equals("O") || label.equals("J") || label.equals("N"));
    }

    @Test
    public void testAddEdgesForPairs() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNB();
        InducedGraph g = new InducedGraph(bn.getNodes());
        GreedyMinEdges.addEdgesForPairs(bn.getNode("M"), g);

        assertTrue(g.getGraph().get(bn.getNode("O")).contains(bn.getNode("N")));
        assertTrue(g.getGraph().get(bn.getNode("O")).contains(bn.getNode("K")));
        assertTrue(g.getGraph().get(bn.getNode("O")).contains(bn.getNode("L")));

        assertTrue(g.getGraph().get(bn.getNode("N")).contains(bn.getNode("L")));
        assertTrue(g.getGraph().get(bn.getNode("N")).contains(bn.getNode("K")));
        assertTrue(g.getGraph().get(bn.getNode("N")).contains(bn.getNode("O")));

        assertTrue(g.getGraph().get(bn.getNode("K")).contains(bn.getNode("O")));
        assertTrue(g.getGraph().get(bn.getNode("K")).contains(bn.getNode("L")));
        assertTrue(g.getGraph().get(bn.getNode("K")).contains(bn.getNode("N")));

        assertTrue(g.getGraph().get(bn.getNode("L")).contains(bn.getNode("N")));
        assertTrue(g.getGraph().get(bn.getNode("L")).contains(bn.getNode("K")));
        assertTrue(g.getGraph().get(bn.getNode("L")).contains(bn.getNode("O")));
    }
}
