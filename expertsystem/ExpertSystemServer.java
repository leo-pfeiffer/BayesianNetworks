import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.text.DecimalFormat;
import bayesiannetwork.BayesianNetworkFactory;
import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import com.google.gson.Gson;

public class ExpertSystemServer {

    public static void main(String[] args) throws IOException {

        int port = 9876;
        if (args.length == 2) {
            port = Integer.parseInt(args[1]);
        }

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        HttpContext context = server.createContext("/");
        context.setHandler(ExpertSystemServer::handleRequest);
        server.start();
        System.out.println("Listening on port " + port + "...");
        System.out.println("Go to http://localhost:" + port + "/ in your browser.");
    }

    private static void handleRequest(HttpExchange exchange) throws IOException {
        if ("GET".equals(exchange.getRequestMethod())) {
            handleGetRequest(exchange);
        } else if ("POST".equals(exchange.getRequestMethod())) {
            handlePostRequest(exchange);
        }
    }

    private static void handleGetRequest(HttpExchange exchange) throws IOException {
        File path = new File("expertsystem/www/index.html");

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

        Agent agent = JsonConf.runConfiguration(conf, BayesianNetworkFactory.createCNX());
        double result = agent.result;
        long runtime = agent.tracker.getRunTime();

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
}
