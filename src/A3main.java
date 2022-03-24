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
                Node variableNode = Parser.getNode(query[0], bn);
                int numericValue = Parser.truthValueToInt(query[1]);
                Order order = Parser.orderFromInput(getOrder(sc), bn);

                // execute query of p(variable=value) with given order of elimination
                Agent ve = new Agent(bn);
                double result = ve.getResult(variableNode, order, numericValue);

                printResult(result);
            }
            break;

            case "P3": {
                BayesianNetwork bn = BayesianNetworkFactory.create(args[1]);

                String[] query = getQueriedNode(sc);
                Node variableNode = Parser.getNode(query[0], bn);
                int numericValue = Parser.truthValueToInt(query[1]);
                Order order = Parser.orderFromInput(getOrder(sc), bn);
                ArrayList<Evidence> evidence = getEvidence(sc, bn);

                // execute query of p(variable=value|evidence) with given order of elimination
                AgentWithEvidence ve = new AgentWithEvidence(bn);
                double result = ve.getResult(variableNode, order, numericValue, evidence);
                printResult(result);
            }
            break;

            case "P4": {
                BayesianNetwork bn = BayesianNetworkFactory.create(args[1]);

                String[] query = getQueriedNode(sc);
                Node variableNode = Parser.getNode(query[0], bn);
                int numericValue = Parser.truthValueToInt(query[1]);
                ArrayList<Evidence> evidence = getEvidence(sc, bn);

                // generate elimination order: orderAlgo either MC or GME
                OrderAlgo orderAlgo = OrderAlgoFactory.create(args[2]);
                Order order = orderAlgo.findOrder(bn, variableNode);
                System.out.println(order);

                AgentWithEvidence ve = new AgentWithEvidence(bn);
                double result = ve.getResult(variableNode, order, numericValue, evidence);
                printResult(result);
            }
            break;
        }
        sc.close();
    }


    //method to obtain the evidence from the user
    private static ArrayList<Evidence> getEvidence(Scanner sc, BayesianNetwork bn) {

        System.out.println("Evidence:");
        String[] line = sc.nextLine().split(" ");

        return Parser.parseEvidence(line, bn);
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

}
