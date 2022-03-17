import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import bayesiannetwork.BayesianNetwork;
import bayesiannetwork.Factor;
import bayesiannetwork.Node;
import org.junit.Test;

public class VariableEliminationTest {

    /**
     * Test on example from lecture slides (L11, p.46)
     * */
    @Test
    public void testPointWiseProduct() {
        Node a = new Node("A");
        Node b = new Node("B");

        b.addParent(a);

        a.getTable().setProbabilities(0.1, 0.9);
        b.getTable().setProbabilities(0.8, 0.2, 0.1, 0.9);

        VariableElimination ve = new VariableElimination(null);

        Factor result = ve.pointWiseProduct(a.getTable(), b.getTable());

        double[] expected = new double[]{0.08, 0.09, 0.02, 0.81};
        for (int i = 0; i < result.getProbabilities().size(); i++) {
            assertEquals(expected[i], result.getProbabilities().get(i), 0.001);
        }
    }

    @Test
    public void testJoinTwoFactors() {
        Node a = new Node("A");
        Node b = new Node("B");

        b.addParent(a);

        a.getTable().setProbabilities(0.1, 0.9);
        b.getTable().setProbabilities(0.8, 0.2, 0.1, 0.9);

        VariableElimination ve = new VariableElimination(null);

        ArrayList<Factor> toSumOut = new ArrayList<>();
        toSumOut.add(a.getTable().copy());
        toSumOut.add(b.getTable().copy());
        Factor result = ve.join(toSumOut);

        double[] expected = new double[]{0.08, 0.09, 0.02, 0.81};
        for (int i = 0; i < result.getProbabilities().size(); i++) {
            assertEquals(expected[i], result.getProbabilities().get(i), 0.001);
        }
    }

    @Test
    public void testMarginalize() {
        Node a = new Node("A");
        Node b = new Node("B");

        Factor table = new Factor();

        table.addColumn(a);
        table.addColumn(b);

        table.setProbabilities(0.08, 0.09, 0.02, 0.81);

        VariableElimination ve = new VariableElimination(null);

        Factor result = ve.marginalize(table, "A");

        assertEquals(2, result.getNumRows());
        assertEquals(b, result.getColumns().get(0).getNode());
        assertEquals(0.17, result.getProbabilities().get(0), 0.001);
        assertEquals(0.83, result.getProbabilities().get(1), 0.001);
    }

    /**
     * Test on example from lecture slides (L11, p.46)
     * */
    @Test
    public void testJoinMarginalizeEx1() {
        Node a = new Node("A");
        Node b = new Node("B");

        b.addParent(a);

        a.getTable().setProbabilities(0.1, 0.9);
        b.getTable().setProbabilities(0.8, 0.2, 0.1, 0.9);

        VariableElimination ve = new VariableElimination(null);
        ArrayList<Factor> toSumOut = new ArrayList<>();
        toSumOut.add(a.getTable().copy());
        toSumOut.add(b.getTable().copy());
        Factor result = ve.joinMarginalize(toSumOut, "A");

        assertEquals(2, result.getNumRows());
        assertEquals(b, result.getColumns().get(0).getNode());
        assertEquals(0.17, result.getProbabilities().get(0), 0.001);
        assertEquals(0.83, result.getProbabilities().get(1), 0.001);
    }

    /**
     * Test on example from lecture slides (L11, p.46)
     * */
    @Test
    public void testGetResultEx1() {

        BayesianNetwork bn = new BayesianNetwork();

        Node a = bn.addNode("A");
        Node b = bn.addNode("B");

        b.addParent(a);

        a.getTable().setProbabilities(0.1, 0.9);
        b.getTable().setProbabilities(0.8, 0.2, 0.1, 0.9);

        VariableElimination ve = new VariableElimination(bn);
        Order order = new Order();
        order.add(a);
        order.add(b);
        double resultTrue = ve.getResult(b, order, 1);
        assertEquals(0.17, resultTrue, 0.001);

        double resultFalse = ve.getResult(b, order, 0);
        assertEquals(0.83, resultFalse, 0.001);
    }
}
