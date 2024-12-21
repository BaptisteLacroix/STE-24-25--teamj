package fr.unice.polytech.equipe.j.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.equipe.j.Application;
import fr.unice.polytech.equipe.j.FlexibleRestServer;
import fr.unice.polytech.equipe.j.HibernateUtil;
import fr.unice.polytech.equipe.j.JacksonConfig;
import fr.unice.polytech.equipe.j.dto.IndividualOrderDTO;
import fr.unice.polytech.equipe.j.dto.OrderDTO;
import fr.unice.polytech.equipe.j.dto.RestaurantDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class RestaurantControllerTest {

    private static final UUID RESTAURANT_UUID = UUID.fromString("3183fa1c-ecd5-49a9-9351-92f75d33fea4"); // TEST Restaurant
    private static final UUID ORDER_UUID = UUID.fromString("178225f2-9f08-4a7f-add2-3783e89ffa6b"); // TEST Order
    private static final UUID INDIVIDUAL_ORDER_UUID = UUID.fromString("f78ed5e2-face-43c3-a60e-7f703bd995c3"); // TEST Order
    private static final UUID USER_UUID = UUID.fromString("2e9aedb2-0d83-4304-871e-a89894bd16ba"); // TEST User
    private static final UUID MANAGER_UUID = UUID.fromString("5926a1d4-1831-48ea-9106-b28cc16c9da3"); // TEST Manager
    private static final UUID MENU_ITEM_UUID = UUID.fromString("cdaa1fc4-621b-4b18-89df-1fafd39aadd0");
    private static final String BASE_URL = "http://localhost:5003/api/restaurants";
    private static final String BASE_DATABASE_URL = "http://localhost:5002/api/database";
    private static final String DATABASE_HEALTH_URL = "http://localhost:5002/api/database/health";  // Assuming you have a health check endpoint

    @BeforeAll
    public static void startServer() {
        String userDir = System.getProperty("user.dir");
        String bddModulePath = Paths.get(System.getProperty("user.dir")).getParent().toString();
        System.setProperty("user.dir", bddModulePath + "/bdd");

        // Set the hibernate.cfg.path system property to the test one hibernate-test.cfg.xml
        System.setProperty("hibernate.cfg.path", "hibernate-test.cfg.xml");
        // wait the end of the execution of the main method before continuing
        while (!HibernateUtil.populateDatabase(new String[]{"test"})) {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.setProperty("user.dir", userDir);
        String backendPath = Paths.get(System.getProperty("user.dir")).getParent().toString();
        new Thread(() -> Application.main(
                new String[]{backendPath + "/bdd/target/classes"}
        )).start();

        // Wait for the database API to be available
        waitForDatabaseToStart(DATABASE_HEALTH_URL);

        FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j", 5003);
        server.start();
    }

    // Helper method to wait until the database API is available
    private static void waitForDatabaseToStart(String databaseUrl) {
        boolean isDatabaseUp = false;
        int attempts = 0;
        int maxAttempts = 30;  // Retry for up to 30 seconds (adjust as necessary)

        while (attempts < maxAttempts && !isDatabaseUp) {
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(databaseUrl).openConnection();
                connection.setRequestMethod("GET");
                int statusCode = connection.getResponseCode();
                if (statusCode == 200) {
                    isDatabaseUp = true;
                    System.out.println("Database API is up and running!");
                    return;
                }
                attempts++;
                System.out.println("Database API not available yet (" + statusCode + "). Retrying... (" + attempts + ")");
                try {
                    TimeUnit.SECONDS.sleep(1);  // Wait 1 second before retrying
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            } catch (Exception e) {
                attempts++;
                System.out.println("Database API not available yet. Retrying... (" + attempts + ")");
                try {
                    TimeUnit.SECONDS.sleep(1);  // Wait 1 second before retrying
                } catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                }
            }
        }

        if (!isDatabaseUp) {
            throw new RuntimeException("Database API did not start in time.");
        }
    }

    @Test
    @Order(1)
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
    @Order(2)
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
    @Order(3)
    void testSearchByFoodType() throws Exception {
        String foodType = "Main Course";
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
    @Order(4)
    void testSearchByDeliveryTime() throws Exception {
        LocalDateTime deliveryTime = LocalDateTime.parse("2024-11-30T18:30:13");
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
        assertNotNull(matchingRestaurants);
    }

    @Test
    @Order(5)
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
        assertEquals(201, response.statusCode());
    }

    @Test
    @Order(6)
    void testChangeNumberOfEmployees() throws Exception {
        // Get the restaurant and get the uuid of a slot
        HttpClient client;
        HttpRequest request;
        java.net.http.HttpResponse<String> response;

        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + RESTAURANT_UUID))
                .GET()
                .build();

        response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        assertNotNull(response.body());
        assertEquals(200, response.statusCode());

        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        RestaurantDTO restaurantDTO = objectMapper.readValue(response.body(), RestaurantDTO.class);
        UUID slotUUID = restaurantDTO.getSlots().get(0).getUuid();

        int numberOfEmployees = 5;
        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + RESTAURANT_UUID + "/manager/" + MANAGER_UUID + "/slots/" + slotUUID + "/changeNumberOfEmployees/" + numberOfEmployees))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(201, response.statusCode());
    }

    @Test
    @Order(7)
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
    @Order(8)
    void testCanAddItemToIndividualOrder() throws Exception {
        // Get the individual order change the orderDelivery time save it and test if it can be added
        LocalDateTime deliveryTime = LocalDateTime.now().plusMinutes(20);
        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        HttpClient client;
        HttpRequest request;
        java.net.http.HttpResponse<String> response;

        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_DATABASE_URL + "/orders/individual/" + INDIVIDUAL_ORDER_UUID))
                .GET()
                .build();

        response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        assertNotNull(response.body());
        assertEquals(200, response.statusCode());

        IndividualOrderDTO individualOrderDTO = objectMapper.readValue(response.body(), IndividualOrderDTO.class);
        individualOrderDTO.getDeliveryDetails().setDeliveryTime(deliveryTime);

        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_DATABASE_URL + "/orders/individual/update"))
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
        assertEquals(201, response.statusCode());
    }

    @Test
    @Order(13)
    void testCanAddItemToOrder() throws Exception {
        // Simulate groupOrder delivery time
        LocalDateTime deliveryTime = LocalDateTime.now().plusMinutes(20);
        HttpClient client;
        HttpRequest request;
        java.net.http.HttpResponse<String> response;

        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_DATABASE_URL + "/orders/" + ORDER_UUID))
                .GET()
                .build();

        response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        assertNotNull(response.body());
        assertEquals(200, response.statusCode());

        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + RESTAURANT_UUID + "/orders/" + ORDER_UUID + "/item/" + MENU_ITEM_UUID + "?deliveryTime=" + deliveryTime))
                .POST(HttpRequest.BodyPublishers.noBody())
                .build();

        response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(201, response.statusCode());
    }

    @Test
    @Order(10)
    void testCancelOrder() throws Exception {
        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        HttpClient client;
        HttpRequest request;
        java.net.http.HttpResponse<String> response;

        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_DATABASE_URL + "/orders/individual/" + INDIVIDUAL_ORDER_UUID))
                .GET()
                .build();

        response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        assertNotNull(response.body());
        assertEquals(200, response.statusCode());

        IndividualOrderDTO individualOrderDTO = objectMapper.readValue(response.body(), IndividualOrderDTO.class);
        LocalDateTime deliveryTime = individualOrderDTO.getDeliveryDetails().getDeliveryTime().minusHours(1);
        client = HttpClient.newHttpClient();
        request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + RESTAURANT_UUID + "/orders/" + INDIVIDUAL_ORDER_UUID + "/cancel"))
                .POST(HttpRequest.BodyPublishers.ofString(deliveryTime.toString()))
                .build();

        response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(200, response.statusCode());
    }

    @Test
    @Order(11)
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
    @Order(12)
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
    @Order(9)
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
    @Order(14)
    void testOnOrderPaid() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BASE_URL + "/" + RESTAURANT_UUID + "/orders/" + ORDER_UUID + "/paid"))
                .POST(HttpRequest.BodyPublishers.ofString(""))
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(201, response.statusCode());
    }
}
