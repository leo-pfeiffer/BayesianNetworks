package bayesiannetwork;

import static org.junit.Assert.assertEquals;

import java.util.List;
import org.junit.Test;

public class InducedGraphTest {

    @Test
    public void testBNA() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNA();
        List<Node> nodes = bn.getNodes();
        InducedGraph g = new InducedGraph(nodes);
        assertEquals(g.getGraph().size(), nodes.size());
        assertEquals(1, g.getGraph().get(bn.getNode("A")).size());
        assertEquals(2, g.getGraph().get(bn.getNode("B")).size());
        assertEquals(2, g.getGraph().get(bn.getNode("C")).size());
        assertEquals(1, g.getGraph().get(bn.getNode("D")).size());
    }

    @Test
    public void testBNB() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNB();
        List<Node> nodes = bn.getNodes();
        InducedGraph g = new InducedGraph(nodes);
        assertEquals(g.getGraph().size(), nodes.size());
        assertEquals(1, g.getGraph().get(bn.getNode("J")).size());
        assertEquals(3, g.getGraph().get(bn.getNode("K")).size());
        assertEquals(2, g.getGraph().get(bn.getNode("L")).size());
        assertEquals(4, g.getGraph().get(bn.getNode("M")).size());
        assertEquals(1, g.getGraph().get(bn.getNode("N")).size());
        assertEquals(1, g.getGraph().get(bn.getNode("O")).size());
    }

    @Test
    public void testBNC() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNC();
        List<Node> nodes = bn.getNodes();
        InducedGraph g = new InducedGraph(nodes);
        assertEquals(g.getGraph().size(), nodes.size());
        assertEquals(1, g.getGraph().get(bn.getNode("P")).size());
        assertEquals(4, g.getGraph().get(bn.getNode("Q")).size());
        assertEquals(3, g.getGraph().get(bn.getNode("R")).size());
        assertEquals(5, g.getGraph().get(bn.getNode("S")).size());
        assertEquals(1, g.getGraph().get(bn.getNode("U")).size());
        assertEquals(4, g.getGraph().get(bn.getNode("V")).size());
        assertEquals(2, g.getGraph().get(bn.getNode("Z")).size());
    }
}
