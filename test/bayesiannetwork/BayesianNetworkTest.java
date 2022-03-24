package bayesiannetwork;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class BayesianNetworkTest {

    @Test
    public void testCreate() {
        BayesianNetwork bn = new BayesianNetwork();
        assertNotNull(bn);
    }

    @Test
    public void testAddNode() {
        BayesianNetwork bn = new BayesianNetwork();
        bn.addNode("A");
        assertNotNull(bn.getNode("A"));
    }

    @Test
    public void testGetNodeMap() {
        BayesianNetwork bn = new BayesianNetwork();
        bn.addNode("A");
        assertTrue(bn.getNodeMap().containsKey("A"));
    }

    @Test
    public void testAddEdge() {
        BayesianNetwork bn = new BayesianNetwork();
        bn.addNode("A");
        bn.addNode("B");
        bn.addEdge("A", "B");
        assertTrue(bn.getNode("A").getChildren().contains(bn.getNode("B")));
    }

    @Test
    public void testHasEdge() {
        BayesianNetwork bn = new BayesianNetwork();
        bn.addNode("A");
        bn.addNode("B");
        bn.addEdge("A", "B");
        assertTrue(bn.hasEdge(bn.getNode("A"), bn.getNode("B")));
    }

    @Test
    public void testHadNode() {
        BayesianNetwork bn = new BayesianNetwork();
        bn.addNode("A");
        assertTrue(bn.hasNode("A"));
    }

    @Test
    public void testToString() {
        BayesianNetwork bn = new BayesianNetwork();
        bn.addNode("A");
        bn.addNode("B");
        bn.addEdge("A", "B");
        String expected = "Nodes: ============================================ \n" +
                "A, B\n" +
                "\n" +
                "Edges: ============================================ \n" +
                "A -> B\n" +
                "\n" +
                "Factor: ============================================ \n" +
                "Variable: A\n" +
                "\n" +
                "A\t|\tp(A)\n" +
                "1\t|\tnot set\n" +
                "0\t|\tnot set\n" +
                "\n" +
                "Variable: B\n" +
                "\n" +
                "A\tB\t|\tp(B|A)\n" +
                "1\t1\t|\tnot set\n" +
                "1\t0\t|\tnot set\n" +
                "0\t1\t|\tnot set\n" +
                "0\t0\t|\tnot set\n\n";

        assertEquals(expected, bn.toString());
    }
}
