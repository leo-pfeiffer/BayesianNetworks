import static org.junit.Assert.assertEquals;

import bayesiannetwork.BayesianNetwork;
import org.junit.Test;

/**
 * This is a JUnit port of some of the provided stacschecks.
 * */
public class StacsChecksTest {

    @Test
    public void testBNAP2q1() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNA();
        Order order = new Order();
        order.add(bn.getNode("A"));
        order.add(bn.getNode("B"));
        order.add(bn.getNode("C"));

        VariableElimination ve = new VariableElimination(bn);
        double result = ve.getResult(bn.getNode("D"), order, 1);
        assertEquals(0.57050, result, 0.001);
    }

    @Test
    public void testBNAP2q2() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNA();
        Order order = new Order();
        order.add(bn.getNode("B"));
        order.add(bn.getNode("C"));
        order.add(bn.getNode("A"));

        VariableElimination ve = new VariableElimination(bn);
        double result = ve.getResult(bn.getNode("D"), order, 1);
        assertEquals(0.57050, result, 0.001);
    }

    @Test
    public void testBNBP2q1() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNB();
        Order order = new Order();
        order.add(bn.getNode("J"));
        order.add(bn.getNode("L"));
        order.add(bn.getNode("K"));
        order.add(bn.getNode("M"));
        order.add(bn.getNode("O"));

        VariableElimination ve = new VariableElimination(bn);
        double result = ve.getResult(bn.getNode("N"), order, 1);
        assertEquals(0.39864, result, 0.001);
    }

    @Test
    public void testBNBP2q2() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNB();
        Order order = new Order();
        order.add(bn.getNode("J"));
        order.add(bn.getNode("L"));
        order.add(bn.getNode("K"));
        order.add(bn.getNode("N"));
        order.add(bn.getNode("O"));

        VariableElimination ve = new VariableElimination(bn);
        double result = ve.getResult(bn.getNode("M"), order, 1);
        assertEquals(0.49660, result, 0.001);
    }

    @Test
    public void testBNCP2q1() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNC();
        Order order = new Order();
        order.add(bn.getNode("P"));
        order.add(bn.getNode("R"));
        order.add(bn.getNode("Z"));
        order.add(bn.getNode("S"));
        order.add(bn.getNode("Q"));
        order.add(bn.getNode("V"));

        VariableElimination ve = new VariableElimination(bn);
        double result = ve.getResult(bn.getNode("U"), order, 1);
        assertEquals(0.42755, result, 0.001);
    }


    @Test
    public void testBNCP2q2() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNC();
        Order order = new Order();
        order.add(bn.getNode("P"));
        order.add(bn.getNode("U"));
        order.add(bn.getNode("R"));
        order.add(bn.getNode("Z"));
        order.add(bn.getNode("Q"));
        order.add(bn.getNode("V"));

        VariableElimination ve = new VariableElimination(bn);
        double result = ve.getResult(bn.getNode("S"), order, 1);
        assertEquals(0.49660, result, 0.001);
    }

}
