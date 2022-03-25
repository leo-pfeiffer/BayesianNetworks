import bayesiannetwork.BayesianNetwork;
import bayesiannetwork.BayesianNetworkFactory;
import bayesiannetwork.Node;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.util.ArrayList;

/**
 * The ExperimentReaderWriter class is used to run experiments and write the results to a JSON file
 */
public class ExperimentReaderWriter {

    private static final String OUT_FOLDER = "evaluation/out/";

    public static void main(String[] args) {
        ArrayList<JsonConf> diagnosticConfigurations = createSetups(100, true);
        ArrayList<JsonConf> predictiveConfigurations = createSetups(100, false);
        try {
            ArrayList<Experiment> dResults = runExperiments(diagnosticConfigurations);
            ArrayList<Experiment> pResults = runExperiments(predictiveConfigurations);
            writeJSON(OUT_FOLDER + "diagnostic-results.json", dResults);
            writeJSON(OUT_FOLDER + "predictive-results.json", pResults);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    public static ArrayList<Experiment> runExperiments(ArrayList<JsonConf> configurations) {
        ArrayList<Experiment> experiments = new ArrayList<>();

        for (JsonConf conf : configurations) {
            BayesianNetwork bn = BayesianNetworkFactory.create(conf.getNetwork());
            Agent agent = JsonConf.runConfiguration(conf, bn);
            experiments.add(makeExperiment(conf, agent));
        }
        return experiments;
    }

    public static Experiment makeExperiment(JsonConf conf, Agent agent) {
        return new Experiment(
                agent.getNetwork().getName(),
                conf.getQueryNode(),
                conf.getOrder(),
                conf.getProvidedOrder(),
                conf.getEvidence(),
                agent.tracker.getRunTime(),
                agent.tracker.getMaxFactorSize()
        );
    }

    public static ArrayList<JsonConf> createSetups(int iterations, boolean diagnostic) {
        String[] networks = {"BNA", "BNB", "BNC", "CNX"};
        String[] orders = {"MC", "GME"};
        ArrayList<JsonConf> configurations = new ArrayList<>();
        for (String network : networks) {
            BayesianNetwork bn = BayesianNetworkFactory.create(network);
            for (String order : orders) {
                OrderAlgo orderAlgo = OrderAlgoFactory.create(order);
                for (int i = 0; i < iterations; i++) {
                    Node queryNode = getRandomNode(bn);

                    // diagnostic query: query node has children
                    if (diagnostic) {
                        while (queryNode.getChildren().size() == 0) {
                            queryNode = getRandomNode(bn);
                        }
                    }
                    // predictive query: query node has parents
                    else {
                        while (queryNode.getParents().size() == 0) {
                            queryNode = getRandomNode(bn);
                        }
                    }
                    int numEvidence = (int) (Math.random() * bn.getNodes().size() - 1);
                    StringBuilder evidence = new StringBuilder();
                    for (int j = 0; j < numEvidence; j++) {

                        // diagnostic: evidence is a child of query node
                        Node evidenceNode = getRandomNode(bn, queryNode);
                        if (diagnostic) {
                            while (!evidenceNode.hasAncestor(queryNode)) {
                                evidenceNode = getRandomNode(bn, queryNode);
                            }
                        }
                        // predictive: evidence is ancestor of query node
                        else {
                            while (!queryNode.hasAncestor(evidenceNode)) {
                                evidenceNode = getRandomNode(bn, queryNode);
                            }
                        }

                        evidence.append(evidenceNode.getLabel()).append(":T");
                        if (j < numEvidence - 1) {
                            evidence.append(" ");
                        }
                    }
                    Order orderArr = orderAlgo.findOrder(bn, queryNode);
                    String[] orderStr = new String[orderArr.size()];
                    for (int j = 0; j < orderArr.size(); j++) {
                        orderStr[j] = orderArr.get(j).getLabel();
                    }
                    JsonConf conf = new JsonConf(queryNode.getLabel(), "T", evidence.toString(), order, orderStr);
                    conf.setNetwork(network);
                    configurations.add(conf);
                }
            }
        }
        return configurations;
    }

    private static Node getRandomNode(BayesianNetwork bn, Node notNode) {
        Node node = getRandomNode(bn);
        while (node.equals(notNode)) {
            node = getRandomNode(bn);
        }
        return node;
    }

    private static Node getRandomNode(BayesianNetwork bn) {
        int index = (int) (Math.random() * bn.getNodes().size());
        return bn.getNodes().get(index);
    }

    /**
     * Write a JSON file with the experiment results.
     * */
    public static void writeJSON(String path, ArrayList<Experiment> experiments) {
        try {

            FileWriter writer = new FileWriter(path);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(experiments, writer);
            writer.close();

            System.out.println("Experiment results written to " + path);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
