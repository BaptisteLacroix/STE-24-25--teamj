package fr.unice.polytech.equipe.j;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.*;

public class FlexibleRestServerTest {

    private static FlexibleRestServer server;

    @BeforeAll
    public static void startServer() {
        server = new FlexibleRestServer("fr.unice.polytech.equipe.j", 5003);
        server.start();
    }

    @AfterAll
    public static void stopServer() {
        // Assuming server has a stop method
        server.stop();
    }

    @Test
    void testGetAllRestaurants() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5003/api/restaurants/all"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertTrue(response.body().contains("restaurantName"));
    }

    @Test
    void testCreateRestaurant() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        String requestBody = """
        {
            "restaurantName": "New Restaurant",
            "openingTime": "10:00:00",
            "closingTime": "22:00:00"
        }
        """;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5003/api/restaurants/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertTrue(response.body().contains("Restaurant added successfully"));
    }

    @Test
    void testInvalidPathParam() throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5003/api/restaurants/invalid-uuid"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertTrue(response.body().contains("errorMessage"));
        assertTrue(response.body().contains("Invalid UUID"));
    }

    @Test
    void testMissingQueryParam() throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5003/api/restaurants/search?location="))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertTrue(response.body().contains("errorMessage"));
        assertTrue(response.body().contains("Missing required query parameter"));
    }
}
