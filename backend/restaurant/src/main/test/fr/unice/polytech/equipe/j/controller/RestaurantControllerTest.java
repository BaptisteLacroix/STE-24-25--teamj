package fr.unice.polytech.equipe.j.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.equipe.j.FlexibleRestServer;
import fr.unice.polytech.equipe.j.dto.RestaurantDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestaurantControllerTest {

    private static final UUID RESTAURANT_UUID = UUID.fromString("1a66eb77-3b5f-40e8-ae72-1ea2e5602f01"); // TEST Restaurant
    private static final UUID ORDER_UUID = UUID.fromString("581066b1-074a-4cf9-92f4-32b271fb53be"); // TEST Order
    private static final UUID USER_UUID = UUID.fromString("2e9aedb2-0d83-4304-871e-a89894bd16ba"); // TEST User
    private static final String BASE_URL = "http://localhost:5003/api/restaurants";
    private static final UUID MENU_ITEM_UUID = UUID.fromString("52945451-330c-4547-ac12-d8fc6d41f816");

    @BeforeAll
    public static void startServer() {
        // Setup server before tests
        FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j", 5003);
        server.start();
    }

    @Test
    void testGetAllRestaurants() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/all"))
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(200, response.statusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        List<RestaurantDTO> restaurantDTOList = objectMapper.readValue(response.body(), new TypeReference<List<RestaurantDTO>>() {
        });
        assertNotNull(restaurantDTOList);
    }

    @Test
    void testGetRestaurantById() throws Exception {
        // Get a restaurant from database and get uuid
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + RESTAURANT_UUID))
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(200, response.statusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        RestaurantDTO restaurantDTO = objectMapper.readValue(response.body(), RestaurantDTO.class);
        assertEquals(RESTAURANT_UUID, restaurantDTO.getUuid());
    }

    @Test
    void testSearchByFoodType() throws Exception {
        String foodType = "Mousse au chocolat";
        HttpClient client = HttpClient.newHttpClient();
        String encodedUri = URLEncoder.encode(foodType, StandardCharsets.UTF_8).replace("+", "%20");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/foodType/" + encodedUri))
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(200, response.statusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        List<RestaurantDTO> matchingRestaurants = objectMapper.readValue(response.body(), new TypeReference<List<RestaurantDTO>>() {
        });
        assertNotNull(matchingRestaurants);
    }

    @Test
    void testAddItemToOrder() throws Exception {
        String deliveryTime = "2024-11-26T14:00:00";
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + RESTAURANT_UUID + "/orders/" + ORDER_UUID + "/item/" + MENU_ITEM_UUID + "?deliveryTime=" + deliveryTime))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    void testCancelOrder() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + RESTAURANT_UUID + "/orders/" + ORDER_UUID + "/cancel"))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    void testGetMenu() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + RESTAURANT_UUID + "/menu"))
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    void testGetTotalPrice() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + RESTAURANT_UUID + "/orders/" + ORDER_UUID + "/total-price"))
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    void testIsSlotCapacityAvailable() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + RESTAURANT_UUID + "/slots/available"))
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    void testOnOrderPaid() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + RESTAURANT_UUID + "/orders/" + ORDER_UUID + "/paid"))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(200, response.statusCode());
    }
}
