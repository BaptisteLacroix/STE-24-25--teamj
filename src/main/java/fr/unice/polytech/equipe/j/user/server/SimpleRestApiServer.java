package fr.unice.polytech.equipe.j.user.server;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import fr.unice.polytech.equipe.j.user.CampusUser;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SimpleRestApiServer {
    // Temporary storage for users
    private static Map<String, CampusUser> users = new HashMap<>();

    public static void main(String[] args) throws IOException {
        // Create an HTTP server that listens on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Define endpoints
        server.createContext("/user", new UserHandler());
        server.createContext("/transaction", new TransactionHandler());

        // Start the server
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started on port 8080");
    }

    // Getter for the users map to access it in UserHandler
    public static Map<String, CampusUser> getUsers() {
        return users;
    }
}

class UserHandler implements HttpHandler {
    private final Gson gson = new Gson();

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        int statusCode = 200; // Default status code
        String method = exchange.getRequestMethod();

        try (OutputStream os = exchange.getResponseBody()) {
            if ("GET".equalsIgnoreCase(method)) {
                // GET request to retrieve all user names
                response = gson.toJson(SimpleRestApiServer.getUsers());
                System.out.println(response);
                System.out.println("GET /user - Returning user list.");

            } else if ("POST".equalsIgnoreCase(method)) {
                // POST request to create a new user
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                try {
                    CampusUser newUser = gson.fromJson(requestBody, CampusUser.class);
                    if (newUser.getName() == null || newUser.getName().isEmpty()) {
                        System.err.println("POST /user - Error: User name is missing or empty in the request.");
                        response = "User name is required.";
                        statusCode = 400;
                    } else {
                        SimpleRestApiServer.getUsers().put(newUser.getName(), newUser);
                        response = "User created successfully";
                        System.out.println("POST /user - User created: " + newUser.getName());
                        System.out.println(SimpleRestApiServer.getUsers());
                        statusCode = 201;
                    }
                } catch (JsonSyntaxException e) {
                    System.err.println("POST /user - Error: Invalid JSON format.");
                    response = "Invalid JSON format";
                    statusCode = 400;
                }

            } else {
                response = "Unsupported HTTP method";
                System.err.println("Error: Unsupported HTTP method - " + method);
                statusCode = 405;
            }

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, response.getBytes().length);
            os.write(response.getBytes());

        } catch (Exception e) {
            response = "Internal Server Error";
            System.err.println("Internal Server Error: " + e.getMessage());
            e.printStackTrace();
            exchange.sendResponseHeaders(500, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
        }
    }
}

class TransactionHandler implements HttpHandler {
    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String response = "";
        int statusCode = 200; // Default status code
        String method = exchange.getRequestMethod();

        try (OutputStream os = exchange.getResponseBody()) {
            if ("GET".equalsIgnoreCase(method)) {
                // Handle retrieving transactions
                response = "List of transactions"; // Replace with actual transaction retrieval
                System.out.println("GET /transaction - Returning list of transactions.");

            } else if ("POST".equalsIgnoreCase(method)) {
                // Handle creating a new transaction
                String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                System.out.println("POST /transaction - Transaction request body received: " + requestBody);

                response = "Transaction created successfully";
                statusCode = 201;
            } else {
                response = "Unsupported HTTP method";
                System.err.println("Error: Unsupported HTTP method - " + method);
                statusCode = 405;
            }

            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, response.getBytes().length);
            os.write(response.getBytes());

        } catch (Exception e) {
            response = "Internal Server Error";
            System.err.println("Internal Server Error: " + e.getMessage());
            e.printStackTrace();
            exchange.sendResponseHeaders(500, response.getBytes().length);
            exchange.getResponseBody().write(response.getBytes());
        }
    }
}
