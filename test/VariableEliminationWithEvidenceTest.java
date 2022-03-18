import static org.junit.Assert.*;

import java.util.ArrayList;
import bayesiannetwork.BayesianNetwork;
import bayesiannetwork.Factor;
import bayesiannetwork.Node;
import org.junit.Test;

public class VariableEliminationWithEvidenceTest {

    /**
     * Using example L12 p.26
     * */
    @Test
    public void pruneOrder() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNB();
        Order order = new Order();
        order.add(bn.getNode("O"));
        order.add(bn.getNode("J"));
        order.add(bn.getNode("M"));
        order.add(bn.getNode("L"));
        order.add(bn.getNode("N"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("O"), 1));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);

        ve.pruneOrder(order, bn.getNode("K"), evidence);

        assertEquals(4, order.size());
        assertEquals(bn.getNode("O"), order.get(0));
        assertEquals(bn.getNode("J"), order.get(1));
        assertEquals(bn.getNode("M"), order.get(2));
        assertEquals(bn.getNode("L"), order.get(3));
    }

    @Test
    public void joinProjectEvidenceSingleEvidence() {
        Node a = new Node("A");
        Node b = new Node("B");
        Node c = new Node("C");
        Node d = new Node("D");
        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(a, 1));

        ArrayList<Factor> factors = new ArrayList<>();
        Factor f1 = new Factor(a);
        f1.addColumn(b);
        f1.setProbabilities(0.5, 0.5, 0.5, 0.5);

        Factor f2 = new Factor(c);
        f2.addColumn(a);
        f2.setProbabilities(0.5, 0.5, 0.5, 0.5);

        factors.add(f1);
        factors.add(f2);

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(null);
        ve.projectEvidence(factors, evidence);

        double[] expected = {0, 0.5, 0, 0.5};
        for (int i = 0; i < f1.getNumRows(); i++) {
            assertEquals(expected[i], f1.getProbabilities().get(i), 0.001);
        }

        for (Double p : f2.getProbabilities()) {
            assertEquals(0.5, p, 0.001);
        }
    }

    @Test
    public void testProjectEvidenceMultiEvidence() {
        // todo
    }

    // todo join marginalise

}