import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.text.DecimalFormat;
import java.util.ArrayList;
import bayesiannetwork.BayesianNetwork;
import bayesiannetwork.BayesianNetworkFactory;
import bayesiannetwork.Node;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.google.gson.Gson;

public class ExtensionServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        HttpContext context = server.createContext("/");
        context.setHandler(ExtensionServer::handleRequest);
        server.start();
        System.out.println("Listening on port 8080. Go to http://localhost:8080/...");
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            handleGetRequest(exchange);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            handlePostRequest(exchange);
        }
    }

    private static void handleGetRequest(HttpExchange exchange) throws IOException {
        File path = new File("www/index.html");

        Headers h = exchange.getResponseHeaders();
        h.add("Content-Type", "text/html");

        OutputStream out = exchange.getResponseBody();

        if (path.exists()) {
            exchange.sendResponseHeaders(200, path.length());
            out.write(Files.readAllBytes(path.toPath()));
        } else {
            System.err.println("File not found: " + path.getAbsolutePath());
            exchange.sendResponseHeaders(404, 0);
            out.write("404 File not found.".getBytes());
        }
        out.close();
    }

    private static void handlePostRequest(HttpExchange exchange) throws IOException {

        JsonConf conf;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
            br.close();
            String confString = sb.toString();
            System.out.println("Received: " + confString);
            conf = readJSON(confString);
            assert conf != null;
        } catch (IOException e) {
            e.printStackTrace();
            OutputStream out = exchange.getResponseBody();
            String response = "Error: " + e.getMessage();
            exchange.sendResponseHeaders(200, response.length());
            out.write(response.getBytes());
            out.close();
            return;
        }

        double startTime = System.currentTimeMillis();
        double result = runConfiguration(conf);
        double runtime = System.currentTimeMillis() - startTime;

        DecimalFormat dd = new DecimalFormat("#0.00000");

        String response = "{" +
                    "\"result\": " + dd.format(result) + "," +
                    "\"runtime\": " + dd.format(runtime) +
                "}";

        System.out.println("Result: " + dd.format(result));
        System.out.println("Runtime: " + dd.format(runtime) + "\n");

        OutputStream out = exchange.getResponseBody();
        exchange.sendResponseHeaders(200, response.length());
        out.write(response.getBytes());
        out.close();
    }

    private static JsonConf readJSON(String json) {
        try {
            Gson gson = new Gson();
            return gson.fromJson(json, (Type) JsonConf.class);

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static double runConfiguration(JsonConf conf) {
        BayesianNetwork bn = BayesianNetworkFactory.createCNX();

        Node node = bn.getNode(conf.getQueryNode());
        System.out.println("Query node: " + node.getLabel());

        int value = Parser.truthValueToInt(conf.getQueryValue());
        System.out.println("Query value: " + value);

        ArrayList<Evidence> evidence = new ArrayList<>();
        if (!conf.getEvidence().equals(""))  {
            evidence = Parser.parseEvidence(conf.getEvidence().split(" "), bn);
        }
        System.out.println("Evidence: " + evidence);

        Order order;
        if (conf.getOrder().equals("Custom")) {
            order = Parser.orderFromInput(conf.getProvidedOrder(), bn);
            System.out.println("Order: " + order);
        } else {
            OrderAlgo orderAlgo = OrderAlgoFactory.create(conf.getOrder());
            order = orderAlgo.findOrder(bn, node);
            System.out.println(conf.getOrder() + " " + order);
        }

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);

        return ve.getResult(node, order, value, evidence);
    }
}
