import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.util.ArrayList;
import bayesiannetwork.BayesianNetwork;
import bayesiannetwork.BayesianNetworkFactory;
import bayesiannetwork.Node;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class ExtensionServer {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        HttpContext context = server.createContext("/");
        context.setHandler(ExtensionServer::handleRequest);
        server.start();
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
        System.out.println("POST request received.");

        try (BufferedReader br = new BufferedReader(new InputStreamReader(exchange.getRequestBody()))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = br.readLine()) != null) {
                sb.append(line);
                System.out.println(line);
            }
            JsonConf conf = readJSON(sb.toString());
            // double result = runConfiguration(conf);
            double result = 0.0123;

            String response = "{" + "\"result\": " + result + "}";

            OutputStream out = exchange.getResponseBody();
            exchange.sendResponseHeaders(200, response.length());
            out.write(String.valueOf(response).getBytes());
            out.close();


        } catch (IOException e) {
            e.printStackTrace();
        }
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
        int value = Parser.truthValueToInt(conf.getQueryValue());
        ArrayList<Evidence> evidence = Parser.parseEvidence(conf.getEvidence().split(" "), bn);

        Order order;
        if (conf.getOrder() == "Custom") {
            order = Parser.orderFromInput(conf.getProvidedOrder().split(","), bn);
        } else {
            OrderAlgo orderAlgo = OrderAlgoFactory.create(conf.getOrder());
            order = orderAlgo.findOrder(bn, node);
        }

        VariableEliminationWithEvidence ve = new VariableEliminationWithEvidence(bn);
        return ve.getResult(node, order, value, evidence);
    }
}
