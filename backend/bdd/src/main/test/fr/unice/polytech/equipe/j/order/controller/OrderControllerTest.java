package fr.unice.polytech.equipe.j.order.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.equipe.j.FlexibleRestServer;
import fr.unice.polytech.equipe.j.JacksonConfig;
import fr.unice.polytech.equipe.j.order.dao.DeliveryLocationDAO;
import fr.unice.polytech.equipe.j.order.dao.OrderDAO;
import fr.unice.polytech.equipe.j.order.dto.DeliveryDetailsDTO;
import fr.unice.polytech.equipe.j.order.dto.DeliveryLocationDTO;
import fr.unice.polytech.equipe.j.order.dto.IndividualOrderDTO;
import fr.unice.polytech.equipe.j.order.dto.OrderDTO;
import fr.unice.polytech.equipe.j.order.dto.OrderStatus;
import fr.unice.polytech.equipe.j.order.dto.PaymentMethod;
import fr.unice.polytech.equipe.j.order.mapper.DeliveryLocationMapper;
import fr.unice.polytech.equipe.j.order.mapper.OrderMapper;
import fr.unice.polytech.equipe.j.restaurant.dao.RestaurantDAO;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.SlotDTO;
import fr.unice.polytech.equipe.j.restaurant.mapper.RestaurantMapper;
import fr.unice.polytech.equipe.j.user.dao.CampusUserDAO;
import fr.unice.polytech.equipe.j.user.dto.CampusUserDTO;
import fr.unice.polytech.equipe.j.user.mapper.CampusUserMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import static fr.unice.polytech.equipe.j.JacksonConfig.configureObjectMapper;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderControllerTest {
    private static final UUID RESTAURANT_ID = UUID.fromString("3183fa1c-ecd5-49a9-9351-92f75d33fea4"); // TEST Restaurant
    private static final UUID ORDER_ID = UUID.fromString("178225f2-9f08-4a7f-add2-3783e89ffa6b"); // TEST Order
    private static final UUID INDIVIDUAL_ID = UUID.fromString("f78ed5e2-face-43c3-a60e-7f703bd995c3"); // TEST Order
    private static final UUID USER_ID = UUID.fromString("1aeb4480-305a-499d-885c-7d2d9f99153b"); // TEST User
    private static final UUID DELIVERY_ID = UUID.fromString("774fcc38-ff40-4cc8-8722-a20bca38338d"); // TEST Delivery
    private static final UUID LOCATION_ID = UUID.fromString("10a83413-1b4a-4184-b082-238d073e6126"); // TEST Location
    private static final FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j", 5004);
    private static final String ORDER_PATH = "http://localhost:5004/api/database/orders/";

    // Start the server
    @BeforeAll
    public static void startServer() {
        // Start the server (assuming the server is already started, this is just an example)
        server.start();
    }

    private CampusUserDTO getCampusUser() {
        CampusUserDTO newUser = new CampusUserDTO();
        newUser.setName("John Doe");
        newUser.setId(USER_ID);
        newUser.setBalance(100.0);
        newUser.setTransactions(List.of());
        newUser.setOrdersHistory(List.of());
        newUser.setDefaultPaymentMethod(PaymentMethod.CREDIT_CARD);
        return newUser;
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
        List<SlotDTO> slotDTOS = List.of(new SlotDTO(UUID.randomUUID(), 0, 9000, LocalDateTime.now(), Duration.of(30, ChronoUnit.MINUTES), 5));
        return new RestaurantDTO(RESTAURANT_ID, "TEST", LocalDateTime.now(), LocalDateTime.now().plusHours(2), slotDTOS, menuDTO, new LinkedHashMap<>());
    }

    private OrderDTO getOrder() {
        RestaurantDAO.save(RestaurantMapper.toEntity(getRestaurant()));
        CampusUserDAO.save(CampusUserMapper.toEntity(getCampusUser()));

        CampusUserDTO userDTO = CampusUserMapper.toDTO(CampusUserDAO.getUserById(USER_ID));
        RestaurantDTO restaurantDTO = RestaurantMapper.toDTO(RestaurantDAO.getRestaurantById(RESTAURANT_ID));

        return new OrderDTO(ORDER_ID, restaurantDTO.getId(), userDTO.getId(), List.of(restaurantDTO.getMenu().getItems().getFirst()), OrderStatus.PENDING.name(), restaurantDTO.getSlots().getFirst());
    }

    private DeliveryDetailsDTO getDeliveryDetails() {
        DeliveryLocationDTO deliveryLocationDTO = new DeliveryLocationDTO(LOCATION_ID,
                "Campus Main Gate",
                "123 Campus Street");
        DeliveryLocationDAO.save(DeliveryLocationMapper.toEntity(deliveryLocationDTO));
        return new DeliveryDetailsDTO(DELIVERY_ID, deliveryLocationDTO, LocalDateTime.now());
    }

    private IndividualOrderDTO getIndividualOrder() {
        RestaurantDAO.save(RestaurantMapper.toEntity(getRestaurant()));
        CampusUserDAO.save(CampusUserMapper.toEntity(getCampusUser()));

        CampusUserDTO userDTO = CampusUserMapper.toDTO(CampusUserDAO.getUserById(USER_ID));
        RestaurantDTO restaurantDTO = RestaurantMapper.toDTO(RestaurantDAO.getRestaurantById(RESTAURANT_ID));

        return new IndividualOrderDTO(new OrderDTO(INDIVIDUAL_ID, restaurantDTO.getId(), userDTO.getId(), List.of(restaurantDTO.getMenu().getItems().getFirst()), OrderStatus.PENDING.name(),  restaurantDTO.getSlots().getFirst()), getDeliveryDetails());
    }

    @Test
    void testGetAllOrders() throws Exception {
        OrderDAO.save(OrderMapper.toEntity(getOrder()));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ORDER_PATH + "all"))
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(200, response.statusCode());

        // Parse the response body into a list of CampusUserDTO objects.
        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        List<OrderDTO> orderDTOList = objectMapper.readValue(response.body(), new TypeReference<List<OrderDTO>>() {
        });

        // Assert that the user list is not null or empty.
        assertNotNull(orderDTOList);
        assertEquals(OrderDAO.getAllOrders().size(), orderDTOList.size());
    }


    @Test
    void testCreateOrder() throws Exception {
        OrderDTO orderDTO = getOrder();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ORDER_PATH + "create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(JacksonConfig.configureObjectMapper().writeValueAsString(orderDTO)))
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        assertEquals(orderDTO.getId(), objectMapper.readValue(response.body(), UUID.class));
    }

    @Test
    void testGetOrderById() throws Exception {
        OrderDAO.save(OrderMapper.toEntity(getOrder()));
        // Create the HttpClient to send the request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ORDER_PATH + ORDER_ID))
                .GET()
                .build();

        // Send the request and get the response
        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(200, response.statusCode());

        // Parse the response body to OrderDTO
        ObjectMapper objectMapper = JacksonConfig.configureObjectMapper();
        OrderDTO orderDTO = objectMapper.readValue(response.body(), OrderDTO.class);

        // Assert that the ID in the response matches the mock ID
        assertEquals(ORDER_ID, orderDTO.getId());
    }


    @Test
    void testUpdateOrder() throws Exception {
        OrderDTO orderDTO = getOrder();
        OrderDAO.save(OrderMapper.toEntity(orderDTO));
        orderDTO.setStatus(OrderStatus.VALIDATED.name());

        // Send request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ORDER_PATH + "update"))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(JacksonConfig.configureObjectMapper().writeValueAsString(orderDTO)))
                .build();
        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        UUID orderUUID = JacksonConfig.configureObjectMapper().readValue(response.body(), UUID.class);
        assertEquals(orderDTO.getId(), orderUUID);

        // Get the updated order by id and check status update
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(ORDER_PATH + orderUUID))
                .GET()
                .build();
        java.net.http.HttpResponse<String> getResponse = client.send(getRequest, java.net.http.HttpResponse.BodyHandlers.ofString());
        OrderDTO updatedOrderDTO = JacksonConfig.configureObjectMapper().readValue(getResponse.body(), OrderDTO.class);
        assertEquals(OrderStatus.VALIDATED.name(), updatedOrderDTO.getStatus());
        assertEquals(orderUUID, updatedOrderDTO.getId());
    }


    // Test the DELETE /{id} endpoint
    @Test
    void testDeleteOrder() throws Exception {
        OrderDTO orderDTO = getOrder();
        OrderDAO.save(OrderMapper.toEntity(orderDTO));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ORDER_PATH + orderDTO.getId()))
                .DELETE()
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    void testCreateIndividualOrder() throws Exception {
        // Create an IndividualOrderDTO
        IndividualOrderDTO individualOrderDTO = new IndividualOrderDTO(getIndividualOrder(), getDeliveryDetails());

        ObjectMapper objectMapper = configureObjectMapper();

        // Send request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ORDER_PATH + "individual/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(individualOrderDTO)))
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        assertEquals(individualOrderDTO.getId(), objectMapper.readValue(response.body(), UUID.class));
    }

}
