import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import bayesiannetwork.BayesianNetwork;
import bayesiannetwork.BayesianNetworkFactory;
import bayesiannetwork.Node;

/********************Starter Code
 *
 * This class contains some examples on how to handle the required inputs and outputs 
 *
 * @author at258
 *
 * run with 
 * java A3main <Pn> <NID>
 *
 * Feel free to change and delete parts of the code as you prefer
 *
 */
public class A3main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);

        switch (args[0]) {
            case "P1": {
                BayesianNetwork bn = BayesianNetworkFactory.create(args[1]);
                System.out.println(bn);
            }
            break;

            case "P2": {
                BayesianNetwork bn = BayesianNetworkFactory.create(args[1]);

                String[] query = getQueriedNode(sc);
                Node variableNode = getNode(query[0], bn);
                int numericValue = truthValueToInt(query[1]);
                Order order = orderFromInput(getOrder(sc), bn);

                // execute query of p(variable=value) with given order of elimination
                VariableElimination ve = new VariableElimination(bn);
                double result = ve.getResult(variableNode, order, numericValue);

                printResult(result);
            }
            break;

            case "P3": {
                BayesianNetwork bn = BayesianNetworkFactory.create(args[1]);

                String[] query = getQueriedNode(sc);
                Node variableNode = getNode(query[0], bn);
                int numericValue = truthValueToInt(query[1]);
                Order order = orderFromInput(getOrder(sc), bn);
                ArrayList<Evidence> evidence = getEvidence(sc, bn);

                // execute query of p(variable=value|evidence) with given order of elimination
                VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
                double result = ve.getResult(variableNode, order, numericValue, evidence);
                printResult(result);
            }
            break;

            case "P4": {
                BayesianNetwork bn = BayesianNetworkFactory.create(args[1]);

                String[] query = getQueriedNode(sc);
                Node variableNode = getNode(query[0], bn);
                int numericValue = truthValueToInt(query[1]);
                ArrayList<Evidence> evidence = getEvidence(sc, bn);

                // generate elimination order: orderAlgo either MC or GME
                OrderAlgo orderAlgo = OrderAlgoFactory.create(args[2]);
                Order order = orderAlgo.findOrder(bn, variableNode);
                System.out.println(order);

                VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
                double result = ve.getResult(variableNode, order, numericValue, evidence);
                printResult(result);
            }
            break;
        }
        sc.close();
    }

    //method to obtain the evidence from the user
    private static ArrayList<String[]> getEvidence(Scanner sc) {

        System.out.println("Evidence:");
        ArrayList<String[]> evidence = new ArrayList<>();
        String[] line = sc.nextLine().split(" ");

        for (String st : line) {
            String[] ev = st.split(":");
            evidence.add(ev);
        }
        return evidence;
    }

    //method to obtain the evidence from the user
    private static ArrayList<Evidence> getEvidence(Scanner sc, BayesianNetwork bn) {

        System.out.println("Evidence:");
        ArrayList<Evidence> evidence = new ArrayList<>();
        String[] line = sc.nextLine().split(" ");

        for (String st : line) {
            String[] ev = st.split(":");
            int truthValue = truthValueToInt(ev[1]);
            Node node = getNode(ev[0], bn);
            evidence.add(new Evidence(node, truthValue));
        }
        return evidence;
    }


    //method to obtain the order from the user
    private static String[] getOrder(Scanner sc) {

        System.out.println("Order:");
        return sc.nextLine().split(",");
    }


    //method to obtain the queried node from the user
    private static String[] getQueriedNode(Scanner sc) {

        System.out.println("Query:");

        return sc.nextLine().split(":");

    }

    //method to format and print the result
    private static void printResult(double result) {

        DecimalFormat dd = new DecimalFormat("#0.00000");
        System.out.println(dd.format(result));
    }

    private static int truthValueToInt(String value) {
        switch (value) {
            case "T":
                return 1;
            case "F":
                return 0;
            default:
                throw new IllegalArgumentException("Invalid value: " + value);
        }
    }

    private static Order orderFromInput(String[] orderRaw, BayesianNetwork bn) {
        Order order = new Order();
        for (String n : orderRaw) {
            Node node = bn.getNode(n);
            if (node == null) {
                throw new IllegalArgumentException("Invalid node: " + n);
            }
            order.add(node);
        }
        return order;
    }

    private static Node getNode(String variable, BayesianNetwork bn) {
        Node variableNode = bn.getNode(variable);
        if (variableNode == null) {
            throw new IllegalArgumentException("Invalid node: " + variable);
        }
        return variableNode;
    }

}
