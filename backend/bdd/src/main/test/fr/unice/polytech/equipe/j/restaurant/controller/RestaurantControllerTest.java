package fr.unice.polytech.equipe.j.restaurant.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.util.JSONPObject;
import fr.unice.polytech.equipe.j.FlexibleRestServer;
import fr.unice.polytech.equipe.j.HibernateUtil;
import fr.unice.polytech.equipe.j.restaurant.dao.RestaurantDAO;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.RestaurantDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RestaurantControllerTest {

    @BeforeAll
    public static void startServer() {
        FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j", 5003);
        server.start();
    }

    @Test
    void testGetRestaurantById() throws JsonProcessingException {
        UUID id = RestaurantDAO.getAllRestaurants().getFirst().getId();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5003/api/restaurants/" + id))
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = null;
        try {
            response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertNotNull(response.body());
        assertEquals(200, response.statusCode());
        ObjectMapper objectMapper = new ObjectMapper();
        RestaurantDTO restaurantDTO = objectMapper.readValue(response.body(), RestaurantDTO.class);
        assertEquals(id, restaurantDTO.getRestaurantId());
    }

    @Test
    void testGetAllRestaurants() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5003/api/restaurants/all"))
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());

        ObjectMapper objectMapper = new ObjectMapper();
        List<RestaurantDTO> restaurantDTO = objectMapper.readValue(response.body(), List.class);
        System.out.println(restaurantDTO);

        assertEquals(200, response.statusCode());
    }

    @Test
    void testCreateRestaurant() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        List<MenuItemDTO> itemsRestaurant = List.of(
                new MenuItemDTO(UUID.randomUUID(), "TEST", 0, 12.50),
                new MenuItemDTO(UUID.randomUUID(), "TEST", 1, 25.00),
                new MenuItemDTO(UUID.randomUUID(), "TEST", 2, 8.00)
        );
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setItems(itemsRestaurant);
        RestaurantDTO restaurantDTO = new RestaurantDTO(UUID.randomUUID(), "TEST", null, null, new ArrayList<>(), menuDTO);
        ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(restaurantDTO);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5003/api/restaurants/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertTrue(response.body().contains("Restaurant created successfully"));
    }

    @Test
    void testInvalidPathParam() throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5003/api/restaurants/invalid-uuid"))
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

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

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertTrue(response.body().contains("errorMessage"));
        assertTrue(response.body().contains("Missing required query parameter"));
    }
}
