package fr.unice.polytech.equipe.j.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.equipe.j.FlexibleRestServer;
import fr.unice.polytech.equipe.j.JacksonConfig;
import fr.unice.polytech.equipe.j.dto.IndividualOrderDTO;
import fr.unice.polytech.equipe.j.dto.RestaurantDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class RestaurantControllerTest {

    private static final UUID RESTAURANT_UUID = UUID.fromString("3183fa1c-ecd5-49a9-9351-92f75d33fea4"); // TEST Restaurant
    private static final UUID ORDER_UUID = UUID.fromString("178225f2-9f08-4a7f-add2-3783e89ffa6b"); // TEST Order
    private static final UUID INDIVIDUAL_ORDER_UUID = UUID.fromString("f78ed5e2-face-43c3-a60e-7f703bd995c3"); // TEST Order
    private static final UUID USER_UUID = UUID.fromString("2e9aedb2-0d83-4304-871e-a89894bd16ba"); // TEST User
    private static final UUID MANAGER_UUID = UUID.fromString("5926a1d4-1831-48ea-9106-b28cc16c9da3"); // TEST Manager
    private static final String BASE_URL = "http://localhost:5003/api/restaurants";
    private static final UUID MENU_ITEM_UUID = UUID.fromString("cdaa1fc4-621b-4b18-89df-1fafd39aadd0");
    private static final UUID SLOT_UUID = UUID.fromString("2f088106-f4e0-43f9-bfd6-4c0b59c6be28");

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

        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
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

        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        RestaurantDTO restaurantDTO = objectMapper.readValue(response.body(), RestaurantDTO.class);
        assertEquals(RESTAURANT_UUID, restaurantDTO.getId());
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

        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        List<RestaurantDTO> matchingRestaurants = objectMapper.readValue(response.body(), new TypeReference<List<RestaurantDTO>>() {
        });
        assertNotNull(matchingRestaurants);
    }

    @Test
    void testSearchByDeliveryTime() throws Exception {
        LocalDateTime deliveryTime = LocalDateTime.parse("2024-11-30T18:30:13");
        System.out.println(deliveryTime);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/deliveryTime/" + deliveryTime.getHour() + "/" + deliveryTime.getMinute()))
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(200, response.statusCode());

        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        List<RestaurantDTO> matchingRestaurants = objectMapper.readValue(response.body(), new TypeReference<List<RestaurantDTO>>() {
        });
        System.out.println(matchingRestaurants);
        assertNotNull(matchingRestaurants);
    }

    @Test
    void testChangeRestaurantsHours() throws Exception {
        LocalDateTime openingHour = LocalDateTime.now();
        LocalDateTime closingHour = LocalDateTime.now().plusHours(6);
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + RESTAURANT_UUID + "/manager/" + MANAGER_UUID + "/changeHours/" + openingHour + "/" + closingHour))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        System.out.println(response.body());
        assertEquals(201, response.statusCode());
    }

    @Test
    void testChangeNumberOfEmployees() throws Exception {
        int numberOfEmployees = 5;
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + RESTAURANT_UUID + "/manager/" + MANAGER_UUID + "/slots/" + SLOT_UUID + "/changeNumberOfEmployees/" + numberOfEmployees))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(201, response.statusCode());
    }

    @Test
    void testCannotAddItemToOrder() throws Exception {
        String deliveryTime = LocalDateTime.parse("2024-12-31T11:00:00").toString();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + RESTAURANT_UUID + "/orders/" + INDIVIDUAL_ORDER_UUID + "/item/" + MENU_ITEM_UUID + "?deliveryTime=" + deliveryTime))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(400, response.statusCode());
    }

    @Test
    void testCanAddItemToOrder() throws Exception {
        // Get the individual order change the orderDelivery time save it and test if it can be added
        LocalDateTime deliveryTime = LocalDateTime.now().plusMinutes(20);
        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        HttpClient client;
        HttpRequest request;
        java.net.http.HttpResponse<String> response;

        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5000/api/database/orders/individual/" + INDIVIDUAL_ORDER_UUID))
                .GET()
                .build();

        System.out.println("SEND REQUEST");
        response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        assertNotNull(response.body());
        assertEquals(200, response.statusCode());

        System.out.println(response.body());
        IndividualOrderDTO individualOrderDTO = objectMapper.readValue(response.body(), IndividualOrderDTO.class);
        individualOrderDTO.getDeliveryDetails().setDeliveryTime(deliveryTime);

        System.out.println(individualOrderDTO);

        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5000/api/database/orders/individual/update"))
                .PUT(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(individualOrderDTO)))
                .build();

        response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(201, response.statusCode());

        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + RESTAURANT_UUID + "/orders/" + INDIVIDUAL_ORDER_UUID + "/item/" + MENU_ITEM_UUID + "?deliveryTime=" + deliveryTime))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        System.out.println(response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    void testCancelOrder() throws Exception {
        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        HttpClient client;
        HttpRequest request;
        java.net.http.HttpResponse<String> response;

        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5000/api/database/orders/individual/" + INDIVIDUAL_ORDER_UUID))
                .GET()
                .build();

        response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        assertNotNull(response.body());
        assertEquals(200, response.statusCode());

        IndividualOrderDTO individualOrderDTO = objectMapper.readValue(response.body(), IndividualOrderDTO.class);
        LocalDateTime deliveryTime = individualOrderDTO.getDeliveryDetails().getDeliveryTime().minusHours(1);
        System.out.println(deliveryTime);
        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + RESTAURANT_UUID + "/orders/" + INDIVIDUAL_ORDER_UUID + "/cancel"))
                .POST(HttpRequest.BodyPublishers.ofString(deliveryTime.toString()))
                .build();

        response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        System.out.println(response.body());
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
                .uri(URI.create(BASE_URL + "/" + RESTAURANT_UUID + "/orders/" + INDIVIDUAL_ORDER_UUID + "/total-price"))
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
                .uri(URI.create(BASE_URL + "/" + RESTAURANT_UUID + "/orders/" + INDIVIDUAL_ORDER_UUID + "/paid"))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(200, response.statusCode());
    }
}
