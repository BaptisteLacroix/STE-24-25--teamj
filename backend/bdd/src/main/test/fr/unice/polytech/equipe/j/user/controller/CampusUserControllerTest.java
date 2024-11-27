package fr.unice.polytech.equipe.j.user.controller;

import fr.unice.polytech.equipe.j.FlexibleRestServer;
import fr.unice.polytech.equipe.j.order.dto.PaymentMethod;
import fr.unice.polytech.equipe.j.user.dao.CampusUserDAO;
import fr.unice.polytech.equipe.j.user.dto.CampusUserDTO;
import fr.unice.polytech.equipe.j.user.mapper.CampusUserMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.jupiter.api.Assertions.*;

public class CampusUserControllerTest {
    private static final UUID USER_ID = UUID.fromString("f5b16f30-b144-49ef-8ba5-9ddf4bd242ba");
    private static final String USER_PATH = "http://localhost:5003/api/database/campusUsers/";

    @BeforeAll
    public static void startServer() {
        // Assuming FlexibleRestServer is a class that starts the server.
        FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j", 5002);
        server.start();
    }

    private CampusUserDTO getCampusUser() {
        CampusUserDTO newUser = new CampusUserDTO();
        newUser.setName("John Doe");
        newUser.setId(USER_ID);
        newUser.setBalance(100.0);
        newUser.setTransactions(new ArrayList<>());
        newUser.setOrdersHistory(new ArrayList<>());
        newUser.setDefaultPaymentMethod(PaymentMethod.CREDIT_CARD);
        return newUser;
    }

    @Test
    void testCreateUser() throws Exception {
        // Convert the DTO to JSON.
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUser = objectMapper.writeValueAsString(getCampusUser());

        // Create an HTTP client and make a POST request to create the user.
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(USER_PATH + "create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonUser))
                .build();
        // Send the request and get the response.
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
    }

    @Test
    void testGetAllUsers() throws Exception {
        // Create an HTTP client and make a GET request to fetch all users.
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(USER_PATH + "all"))
                .GET()
                .build();

        // Send the request and get the response.
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert that the response is not null and has a 200 status code.
        assertNotNull(response.body());
        assertEquals(200, response.statusCode());

        // Parse the response body into a list of CampusUserDTO objects.
        ObjectMapper objectMapper = new ObjectMapper();
        List<CampusUserDTO> userDTOList = objectMapper.readValue(response.body(), new TypeReference<List<CampusUserDTO>>() {});

        // Assert that the user list is not null or empty.
        assertNotNull(userDTOList);
        assertEquals(CampusUserDAO.getAll().size(), userDTOList.size());
    }

    @Test
    void testGetUserById() throws Exception {
        CampusUserDAO.save(CampusUserMapper.toEntity(getCampusUser()));
        // Make a GET request to fetch the user by their UUID.
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(USER_PATH + USER_ID))
                .GET()
                .build();

        // Send the request and get the response.
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert the response is not null and status is 200.
        assertNotNull(response.body());
        assertEquals(200, response.statusCode());

        // Parse the response to a CampusUserDTO object.
        ObjectMapper objectMapper = new ObjectMapper();
        CampusUserDTO userDTO = objectMapper.readValue(response.body(), CampusUserDTO.class);

        // Assert that the UUID of the returned user matches the requested UUID.
        assertEquals(USER_ID, userDTO.getId());
    }

    @Test
    void testGetUserByIdNotFound() throws Exception {
        // Generate a random UUID that doesn't exist in the database.
        UUID nonExistingUserId = UUID.randomUUID();

        // Make a GET request to fetch the non-existing user.
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(USER_PATH + nonExistingUserId))
                .GET()
                .build();

        // Send the request and get the response.
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Assert the status code is 404 (Not Found) for a non-existing user.
        assertEquals(404, response.statusCode());
    }

    @Test
    void testDeleteUserById() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonUser = objectMapper.writeValueAsString(getCampusUser());

        // Create the user first.
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest createRequest = HttpRequest.newBuilder()
                .uri(URI.create(USER_PATH + "create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonUser))
                .build();
        HttpResponse<String> createResponse = client.send(createRequest, HttpResponse.BodyHandlers.ofString());

        // Assert that the create request was successful.
        assertEquals(201, createResponse.statusCode());

        // Fetch the ID of the newly created user (assuming it was created).
        UUID userId = objectMapper.readValue(createResponse.body(), UUID.class);

        // Now, make a DELETE request to delete the user.
        HttpRequest deleteRequest = HttpRequest.newBuilder()
                .uri(URI.create(USER_PATH + userId))
                .DELETE()
                .build();

        HttpResponse<String> deleteResponse = client.send(deleteRequest, HttpResponse.BodyHandlers.ofString());

        // Assert that the response has a 200 status code (successful deletion).
        assertEquals(200, deleteResponse.statusCode());

        // Verify that the user has been deleted (optional but good practice).
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(USER_PATH + userId))
                .GET()
                .build();
        HttpResponse<String> getResponse = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

        // Assert that after deletion, the user cannot be found (404 Not Found).
        assertEquals(404, getResponse.statusCode());
    }


}
