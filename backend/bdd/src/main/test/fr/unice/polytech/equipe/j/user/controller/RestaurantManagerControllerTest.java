package fr.unice.polytech.equipe.j.user.controller;

import fr.unice.polytech.equipe.j.FlexibleRestServer;
import fr.unice.polytech.equipe.j.JacksonConfig;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.user.dao.RestaurantManagerDAO;
import fr.unice.polytech.equipe.j.user.dto.RestaurantManagerDTO;
import fr.unice.polytech.equipe.j.user.mapper.RestaurantManagerMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.*;

public class RestaurantManagerControllerTest {
    private static final UUID MANAGER_ID = UUID.fromString("f5b16f30-b144-49ef-8ba5-9ddf4bd242ba");
    private static final UUID RESTAURANT_ID = UUID.fromString("3183fa1c-ecd5-49a9-9351-92f75d33fea4");
    private static final String MANAGER_PATH = "http://localhost:5001/api/database/managers/";

    @BeforeAll
    public static void startServer() {
        // Assuming FlexibleRestServer is a class that starts the server.
        FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j", 5001);
        server.start();
    }

    private RestaurantDTO getRestaurant() {
        List<MenuItemDTO> itemsRestaurant = List.of(
                new MenuItemDTO(UUID.randomUUID(), "TEST", 0, 12.50, "Appetizer"),
                new MenuItemDTO(UUID.randomUUID(), "TEST", 1, 25.00, "Main Course"),
                new MenuItemDTO(UUID.randomUUID(), "TEST", 2, 8.00, "Dessert")
        );
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setUuid(UUID.randomUUID());
        menuDTO.setItems(itemsRestaurant);
        return new RestaurantDTO(RESTAURANT_ID, "TEST", null, null, new ArrayList<>(), menuDTO, new LinkedHashMap<>());
    }

    private RestaurantManagerDTO getRestaurantManager() {
        RestaurantManagerDTO newManager = new RestaurantManagerDTO();
        newManager.setName("Alice Smith");
        newManager.setEmail("alice.smith@example.com");
        newManager.setId(MANAGER_ID);
        newManager.setRestaurant(getRestaurant());
        return newManager;
    }

    @Test
    void testCreateManager() throws Exception {
        // Convert the DTO to JSON.
        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        String jsonManager = objectMapper.writeValueAsString(getRestaurantManager());

        // Create an HTTP client and make a POST request to create the manager.
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MANAGER_PATH + "create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonManager))
                .build();

        // Send the request and get the response.
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert that the response has a 201 status code (created successfully).
        assertEquals(201, response.statusCode());
    }

    @Test
    void testGetAllManagers() throws Exception {
        RestaurantManagerDAO.save(RestaurantManagerMapper.toEntity(getRestaurantManager()));
        // Create an HTTP client and make a GET request to fetch all managers.
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MANAGER_PATH + "all"))
                .GET()
                .build();

        // Send the request and get the response.
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert that the response is not null and has a 200 status code.
        assertNotNull(response.body());
        assertEquals(200, response.statusCode());

        // Parse the response body into a list of RestaurantManagerDTO objects.
        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        List<RestaurantManagerDTO> managerDTOList = objectMapper.readValue(response.body(), new TypeReference<List<RestaurantManagerDTO>>() {});

        // Assert that the manager list is not null or empty.
        assertNotNull(managerDTOList);
        assertEquals(RestaurantManagerDAO.getAll().size(), managerDTOList.size());
    }

    @Test
    void testGetManagerById() throws Exception {
        RestaurantManagerDAO.save(RestaurantManagerMapper.toEntity(getRestaurantManager()));
        // Make a GET request to fetch the manager by their UUID.
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(MANAGER_PATH + MANAGER_ID))
                .GET()
                .build();

        // Send the request and get the response.
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert that the response is not null and has a 200 status code.
        assertNotNull(response.body());
        assertEquals(200, response.statusCode());

        // Parse the response to a RestaurantManagerDTO object.
        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        RestaurantManagerDTO managerDTO = objectMapper.readValue(response.body(), RestaurantManagerDTO.class);

        // Assert that the UUID of the returned manager matches the requested UUID.
        assertEquals(MANAGER_ID, managerDTO.getId());
    }

    @Test
    void testDeleteManagerById() throws Exception {
        RestaurantManagerDAO.save(RestaurantManagerMapper.toEntity(getRestaurantManager()));
        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        String jsonManager = objectMapper.writeValueAsString(getRestaurantManager());
        // Create the manager first.
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create(MANAGER_PATH + "create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonManager))
                .build();
        HttpResponse<String> createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());

        // Assert that the create request was successful.
        assertEquals(201, createResponse.statusCode());

        // Fetch the ID of the newly created manager (assuming it was created).
        UUID managerId = objectMapper.readValue(createResponse.body(), UUID.class);
        // Now, make a DELETE request to delete the manager.
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create(MANAGER_PATH + managerId))
                .DELETE()
                .build();

        HttpResponse<String> deleteResponse = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());
        // Assert that the response has a 200 status code (successful deletion).
        assertEquals(200, deleteResponse.statusCode());
        assertEquals("Manager deleted", deleteResponse.body());
        // Verify that the manager has been deleted (optional but good practice).
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(MANAGER_PATH + managerId))
                .GET()
                .build();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        // Assert that after deletion, the manager cannot be found (404 Not Found).
        assertEquals(404, getResponse.statusCode());
    }
}
