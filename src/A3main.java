import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Scanner;
import bayesiannetwork.BayesianNetwork;
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
                //construct the network in args[1]
                String network = args[1];
                System.out.println("Network " + network);
                BayesianNetwork bn = BayesianNetworkFactory.create(network);
                System.out.println(bn);
            }
            break;

            case "P2": {
                //construct the network in args[1]
                String network = args[1];
                BayesianNetwork bn = BayesianNetworkFactory.create(network);

                String[] query = getQueriedNode(sc);
                String variable = query[0];

                Node variableNode = bn.getNode(variable);
                if (variableNode == null) {
                    throw new IllegalArgumentException("Invalid node: " + variable);
                }

                String value = query[1];

                int numericValue;
                switch (value) {
                    case "T":
                        numericValue = 1;
                        break;
                    case "F":
                        numericValue = 0;
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid value: " + value);
                }

                String[] orderRaw = getOrder(sc);
                Order order = new Order();
                for (String n : orderRaw) {
                    Node node = bn.getNode(n);
                    if (node == null) {
                        throw new IllegalArgumentException("Invalid node: " + n);
                    }
                    order.add(node);
                }

                // execute query of p(variable=value) with given order of elimination
                VariableElimination ve = new VariableElimination(bn, variableNode, order, numericValue);

                // double result = ve.getResult();
                double result = 0.570501;
                printResult(result);
            }
            break;

            case "P3": {
                //construct the network in args[1]
                String[] query = getQueriedNode(sc);
                String variable = query[0];
                String value = query[1];
                String[] order = getOrder(sc);
                ArrayList<String[]> evidence = getEvidence(sc);
                // execute query of p(variable=value|evidence) with given order of elimination
                double result = 0.570501;
                printResult(result);
            }
            break;

            case "P4": {
                //construct the network in args[1]
                String[] query = getQueriedNode(sc);
                String variable = query[0];
                String value = query[1];
                String order = "A,B";
                ArrayList<String[]> evidence = getEvidence(sc);
                // execute query of p(variable=value|evidence) with given order of elimination
                //print the order
                System.out.println(order);
                double result = 0.570501;
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
