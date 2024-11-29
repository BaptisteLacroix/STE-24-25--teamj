package fr.unice.polytech.equipe.j.restaurant.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import fr.unice.polytech.equipe.j.FlexibleRestServer;
import fr.unice.polytech.equipe.j.JacksonConfig;
import fr.unice.polytech.equipe.j.restaurant.dao.RestaurantDAO;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.restaurant.mapper.RestaurantMapper;
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
    private static final UUID RESTAURANT_ID = UUID.fromString("3183fa1c-ecd5-49a9-9351-92f75d33fea4");
    private static final String RESTAURANT_PATH = "http://localhost:5003/api/database/restaurants/";

    @BeforeAll
    public static void startServer() {
        FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j", 5003);
        server.start();
    }

    private RestaurantDTO getRestaurant() {
        List<MenuItemDTO> itemsRestaurant = List.of(
                new MenuItemDTO(UUID.randomUUID(), "TEST", 0, 12.50),
                new MenuItemDTO(UUID.randomUUID(), "TEST", 1, 25.00),
                new MenuItemDTO(UUID.randomUUID(), "TEST", 2, 8.00)
        );
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setUuid(UUID.randomUUID());
        menuDTO.setItems(itemsRestaurant);
        return new RestaurantDTO(RESTAURANT_ID, "TEST", null, null, new ArrayList<>(), menuDTO);
    }

    @Test
    void testGetAllRestaurants() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RESTAURANT_PATH + "all"))
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());

        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        List<RestaurantDTO> restaurantDTOList = objectMapper.readValue(response.body(), new TypeReference<List<RestaurantDTO>>() {
        });
        assertNotNull(restaurantDTOList);
        assertEquals(200, response.statusCode());
    }

    @Test
    void testCreateRestaurant() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        ObjectWriter ow = JacksonConfig.configureObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(getRestaurant());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RESTAURANT_PATH + "create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        UUID restaurantUUID = objectMapper.readValue(response.body(), UUID.class);
        assertEquals(RESTAURANT_ID, restaurantUUID);
    }

    @Test
    void testGetRestaurantById() throws JsonProcessingException {
        RestaurantDAO.save(RestaurantMapper.toEntity(getRestaurant()));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RESTAURANT_PATH + RESTAURANT_ID))
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
        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        RestaurantDTO restaurantDTO = objectMapper.readValue(response.body(), RestaurantDTO.class);
        assertEquals(RESTAURANT_ID, restaurantDTO.getId());
    }

    @Test
    void deleteRestaurant() throws Exception {
        RestaurantDAO.save(RestaurantMapper.toEntity(getRestaurant()));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RESTAURANT_PATH + RESTAURANT_ID))
                .DELETE()
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertTrue(response.body().contains("Restaurant deleted"));
    }
}
