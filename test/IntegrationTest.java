import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import bayesiannetwork.BayesianNetwork;
import bayesiannetwork.BayesianNetworkFactory;
import org.junit.Test;

/**
 * This class tests full calculations of the probabilities. Some tests are ported from the stacscheck tests.
 * */
public class IntegrationTest {

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

    @Test
    public void testBNAP3q1() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNA();
        Order order = new Order();
        order.add(bn.getNode("A"));
        order.add(bn.getNode("B"));
        order.add(bn.getNode("C"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("A"), 1));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        double result = ve.getResult(bn.getNode("D"), order, 1, evidence);
        assertEquals(0.54200, result, 0.001);

        MaxCardinality mc = new MaxCardinality();
        VariableEliminationWithEvidence ve2 = new VariableEliminationWithEvidence(bn);
        Order order2 = mc.findOrder(bn, bn.getNode("D"));
        double result2 = ve2.getResult(bn.getNode("D"), order, 1, evidence);
        assertEquals(0.54200, result2, 0.001);

    }

    @Test
    public void testBNAP3q2() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNA();
        Order order = new Order();
        order.add(bn.getNode("A"));
        order.add(bn.getNode("B"));
        order.add(bn.getNode("C"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("A"), 1));
        evidence.add(new Evidence(bn.getNode("B"), 1));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        double result = ve.getResult(bn.getNode("D"), order, 1, evidence);
        assertEquals(0.58000, result, 0.001);

        MaxCardinality mc = new MaxCardinality();
        VariableEliminationWithEvidence ve2 = new VariableEliminationWithEvidence(bn);
        Order order2 = mc.findOrder(bn, bn.getNode("D"));
        double result2 = ve.getResult(bn.getNode("D"), order2, 1, evidence);
        assertEquals(0.58000, result2, 0.001);

    }

    @Test
    public void testBNAP3q3() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNA();
        Order order = new Order();
        order.add(bn.getNode("A"));
        order.add(bn.getNode("B"));
        order.add(bn.getNode("C"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("C"), 1));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        double result = ve.getResult(bn.getNode("A"), order, 1, evidence);
        assertEquals(0.09831, result, 0.001);

        MaxCardinality mc = new MaxCardinality();
        VariableEliminationWithEvidence ve2 = new VariableEliminationWithEvidence(bn);
        Order order2 = mc.findOrder(bn, bn.getNode("A"));
        double result2 = ve.getResult(bn.getNode("A"), order2, 1, evidence);
        assertEquals(0.09831, result2, 0.001);
    }

    @Test
    public void testBNAP3q4() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNA();
        Order order = new Order();
        order.add(bn.getNode("A"));
        order.add(bn.getNode("B"));
        order.add(bn.getNode("C"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("B"), 1));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        double result = ve.getResult(bn.getNode("D"), order, 1, evidence);
        assertEquals(0.58000, result, 0.001);

        MaxCardinality mc = new MaxCardinality();
        VariableEliminationWithEvidence ve2 = new VariableEliminationWithEvidence(bn);
        Order order2 = mc.findOrder(bn, bn.getNode("D"));
        double result2 = ve.getResult(bn.getNode("D"), order2, 1, evidence);
        assertEquals(0.58000, result2, 0.001);
    }

    @Test
    public void testBNAP3q5() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNA();
        Order order = new Order();
        order.add(bn.getNode("A"));
        order.add(bn.getNode("B"));
        order.add(bn.getNode("C"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("A"), 0));
        evidence.add(new Evidence(bn.getNode("B"), 1));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        double result = ve.getResult(bn.getNode("D"), order, 1, evidence);
        assertEquals(0.58000, result, 0.001);

        MaxCardinality mc = new MaxCardinality();
        VariableEliminationWithEvidence ve2 = new VariableEliminationWithEvidence(bn);
        Order order2 = mc.findOrder(bn, bn.getNode("D"));
        double result2 = ve.getResult(bn.getNode("D"), order2, 1, evidence);
        assertEquals(0.58000, result2, 0.001);
    }

    @Test
    public void testBNBP3q1() {
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
        double result = ve.getResult(bn.getNode("K"), order, 1, evidence);
        assertEquals(0.54385, result, 0.001);

        MaxCardinality mc = new MaxCardinality();
        VariableEliminationWithEvidence ve2 = new VariableEliminationWithEvidence(bn);
        Order order2 = mc.findOrder(bn, bn.getNode("K"));
        double result2 = ve.getResult(bn.getNode("K"), order2, 1, evidence);
        assertEquals(0.54385, result2, 0.001);
    }

    @Test
    public void testBNBP3q2() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNB();
        Order order = new Order();
        order.add(bn.getNode("O"));
        order.add(bn.getNode("M"));
        order.add(bn.getNode("L"));
        order.add(bn.getNode("K"));
        order.add(bn.getNode("N"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("O"), 1));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        double result = ve.getResult(bn.getNode("J"), order, 1, evidence);
        assertEquals(0.04233, result, 0.001);

        MaxCardinality mc = new MaxCardinality();
        VariableEliminationWithEvidence ve2 = new VariableEliminationWithEvidence(bn);
        Order order2 = mc.findOrder(bn, bn.getNode("J"));
        double result2 = ve.getResult(bn.getNode("J"), order2, 1, evidence);
        assertEquals(0.04233, result2, 0.001);
    }

    @Test
    public void testBNBP3q3() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNB();
        Order order = new Order();
        order.add(bn.getNode("J"));
        order.add(bn.getNode("L"));
        order.add(bn.getNode("K"));
        order.add(bn.getNode("M"));
        order.add(bn.getNode("O"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("J"), 1));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        double result = ve.getResult(bn.getNode("N"), order, 1, evidence);
        assertEquals(0.43360, result, 0.001);

        MaxCardinality mc = new MaxCardinality();
        VariableEliminationWithEvidence ve2 = new VariableEliminationWithEvidence(bn);
        Order order2 = mc.findOrder(bn, bn.getNode("N"));
        double result2 = ve.getResult(bn.getNode("N"), order2, 1, evidence);
        assertEquals(0.43360, result2, 0.001);
    }

    @Test
    public void testBNBP3q4() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNB();
        Order order = new Order();
        order.add(bn.getNode("J"));
        order.add(bn.getNode("L"));
        order.add(bn.getNode("K"));
        order.add(bn.getNode("M"));
        order.add(bn.getNode("O"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("J"), 1));
        evidence.add(new Evidence(bn.getNode("L"), 1));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        double result = ve.getResult(bn.getNode("N"), order, 1, evidence);
        assertEquals(0.42400, result, 0.001);

        MaxCardinality mc = new MaxCardinality();
        VariableEliminationWithEvidence ve2 = new VariableEliminationWithEvidence(bn);
        Order order2 = mc.findOrder(bn, bn.getNode("N"));
        double result2 = ve.getResult(bn.getNode("N"), order2, 1, evidence);
        assertEquals(0.42400, result2, 0.001);
    }

    @Test
    public void testBNBP3q5() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNB();
        Order order = new Order();
        order.add(bn.getNode("J"));
        order.add(bn.getNode("L"));
        order.add(bn.getNode("K"));
        order.add(bn.getNode("M"));
        order.add(bn.getNode("O"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("J"), 1));
        evidence.add(new Evidence(bn.getNode("L"), 0));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        double result = ve.getResult(bn.getNode("N"), order, 1, evidence);
        assertEquals(0.45600, result, 0.001);

        MaxCardinality mc = new MaxCardinality();
        VariableEliminationWithEvidence ve2 = new VariableEliminationWithEvidence(bn);
        Order order2 = mc.findOrder(bn, bn.getNode("N"));
        double result2 = ve.getResult(bn.getNode("N"), order2, 1, evidence);
        assertEquals(0.45600, result2, 0.001);
    }

    @Test
    public void testBNCP3q1() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNC();
        Order order = new Order();
        order.add(bn.getNode("P"));
        order.add(bn.getNode("U"));
        order.add(bn.getNode("R"));
        order.add(bn.getNode("S"));
        order.add(bn.getNode("Q"));
        order.add(bn.getNode("V"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("R"), 1));
        evidence.add(new Evidence(bn.getNode("U"), 1));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        double result = ve.getResult(bn.getNode("Z"), order, 1, evidence);
        assertEquals(0.43368, result, 0.001);

        MaxCardinality mc = new MaxCardinality();
        VariableEliminationWithEvidence ve2 = new VariableEliminationWithEvidence(bn);
        Order order2 = mc.findOrder(bn, bn.getNode("Z"));
        double result2 = ve.getResult(bn.getNode("Z"), order2, 1, evidence);
        assertEquals(0.43368, result2, 0.001);
    }

    @Test
    public void testBNCP3q2() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNC();
        Order order = new Order();
        order.add(bn.getNode("U"));
        order.add(bn.getNode("R"));
        order.add(bn.getNode("Z"));
        order.add(bn.getNode("S"));
        order.add(bn.getNode("Q"));
        order.add(bn.getNode("V"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("Z"), 1));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        double result = ve.getResult(bn.getNode("P"), order, 1, evidence);
        assertEquals(0.05509, result, 0.001);

        MaxCardinality mc = new MaxCardinality();
        VariableEliminationWithEvidence ve2 = new VariableEliminationWithEvidence(bn);
        Order order2 = mc.findOrder(bn, bn.getNode("P"));
        double result2 = ve.getResult(bn.getNode("P"), order2, 1, evidence);
        assertEquals(0.05509, result2, 0.001);
    }

    @Test
    public void testBNCP3q3() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNC();
        Order order = new Order();
        order.add(bn.getNode("P"));
        order.add(bn.getNode("U"));
        order.add(bn.getNode("R"));
        order.add(bn.getNode("Z"));
        order.add(bn.getNode("S"));
        order.add(bn.getNode("V"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("Z"), 1));
        evidence.add(new Evidence(bn.getNode("S"), 1));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        double result = ve.getResult(bn.getNode("Q"), order, 1, evidence);

        assertEquals(0.92141, result, 0.001);

        MaxCardinality mc = new MaxCardinality();
        VariableEliminationWithEvidence ve2 = new VariableEliminationWithEvidence(bn);
        Order order2 = mc.findOrder(bn, bn.getNode("Q"));
        double result2 = ve.getResult(bn.getNode("Q"), order2, 1, evidence);
        assertEquals(0.92141, result2, 0.001);
    }

    @Test
    public void testBNCP3q4() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNC();
        Order order = new Order();
        order.add(bn.getNode("P"));
        order.add(bn.getNode("R"));
        order.add(bn.getNode("Z"));
        order.add(bn.getNode("S"));
        order.add(bn.getNode("Q"));
        order.add(bn.getNode("V"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("Z"), 1));
        evidence.add(new Evidence(bn.getNode("Q"), 1));
        evidence.add(new Evidence(bn.getNode("R"), 1));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        double result = ve.getResult(bn.getNode("U"), order, 1, evidence);
        assertEquals(0.34204, result, 0.001);

        MaxCardinality mc = new MaxCardinality();
        VariableEliminationWithEvidence ve2 = new VariableEliminationWithEvidence(bn);
        Order order2 = mc.findOrder(bn, bn.getNode("U"));
        double result2 = ve.getResult(bn.getNode("U"), order2, 1, evidence);
        assertEquals(0.34204, result2, 0.001);
    }

    @Test
    public void testBNBedge1() {
        BayesianNetwork bn = BayesianNetworkFactory.createBNB();
        GreedyMinEdges gme = new GreedyMinEdges();
        Order order = gme.findOrder(bn, bn.getNode("J"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("K"), 1));
        evidence.add(new Evidence(bn.getNode("L"), 1));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        double result = ve.getResult(bn.getNode("J"), order, 1, evidence);
        assertEquals(0.06338, result, 0.001);
    }

    @Test
    public void testCNX1Diagnostic() {
        BayesianNetwork bn = BayesianNetworkFactory.createCNX();
        MaxCardinality mc = new MaxCardinality();
        Order order = mc.findOrder(bn, bn.getNode("M_I"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("Hol"), 0));
        evidence.add(new Evidence(bn.getNode("C_A"), 1));
        evidence.add(new Evidence(bn.getNode("Fir"), 1));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        double result = ve.getResult(bn.getNode("M_I"), order, 1, evidence);
        assertEquals(0.01129, result, 0.001);
    }

    @Test
    public void testCNX2Predictive() {
        BayesianNetwork bn = BayesianNetworkFactory.createCNX();
        MaxCardinality mc = new MaxCardinality();
        Order order = mc.findOrder(bn, bn.getNode("Ale"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("D_R"), 1));
        evidence.add(new Evidence(bn.getNode("Log"), 1));
        evidence.add(new Evidence(bn.getNode("Fir"), 1));
        evidence.add(new Evidence(bn.getNode("Blo"), 1));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        double result = ve.getResult(bn.getNode("Ale"), order, 1, evidence);
        assertEquals(0.95, result, 0.001);
    }


    @Test
    public void testCNX3Predictive() {
        BayesianNetwork bn = BayesianNetworkFactory.createCNX();
        MaxCardinality mc = new MaxCardinality();
        Order order = mc.findOrder(bn, bn.getNode("Ale"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("Hol"), 1));
        evidence.add(new Evidence(bn.getNode("C_A"), 1));
        evidence.add(new Evidence(bn.getNode("Mai"), 1));
        evidence.add(new Evidence(bn.getNode("M_I"), 1));
        evidence.add(new Evidence(bn.getNode("Web"), 1));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        double result = ve.getResult(bn.getNode("Ale"), order, 1, evidence);
        assertEquals(0.5225, result, 0.001);
    }

    @Test
    public void testCNX4Diagnostic() {
        BayesianNetwork bn = BayesianNetworkFactory.createCNX();
        MaxCardinality mc = new MaxCardinality();
        Order order = mc.findOrder(bn, bn.getNode("C_A"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("Ale"), 1));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        double result = ve.getResult(bn.getNode("C_A"), order, 1, evidence);
        assertEquals(0.06314, result, 0.001);
    }

    @Test
    public void testCNX5Diagnostic() {
        BayesianNetwork bn = BayesianNetworkFactory.createCNX();
        MaxCardinality mc = new MaxCardinality();
        Order order = mc.findOrder(bn, bn.getNode("Mai"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("Ale"), 1));
        evidence.add(new Evidence(bn.getNode("M_I"), 1));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        double result = ve.getResult(bn.getNode("Mai"), order, 1, evidence);
        assertEquals(0.1, result, 0.001);
    }

    @Test
    public void testCNX6Diagnostic() {
        BayesianNetwork bn = BayesianNetworkFactory.createCNX();
        MaxCardinality mc = new MaxCardinality();
        Order order = mc.findOrder(bn, bn.getNode("C_A"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("Ale"), 1));
        evidence.add(new Evidence(bn.getNode("D_R"), 1));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        double result = ve.getResult(bn.getNode("C_A"), order, 1, evidence);
        assertEquals(0.05287, result, 0.001);
    }

    @Test
    public void testCNX7Diagnostic() {
        BayesianNetwork bn = BayesianNetworkFactory.createCNX();
        MaxCardinality mc = new MaxCardinality();
        Order order = mc.findOrder(bn, bn.getNode("Mai"));

        ArrayList<Evidence> evidence = new ArrayList<>();
        evidence.add(new Evidence(bn.getNode("Ale"), 1));
        evidence.add(new Evidence(bn.getNode("Fir"), 1));

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        double result = ve.getResult(bn.getNode("Mai"), order, 1, evidence);
        assertEquals(0.00445, result, 0.001);
    }


}
