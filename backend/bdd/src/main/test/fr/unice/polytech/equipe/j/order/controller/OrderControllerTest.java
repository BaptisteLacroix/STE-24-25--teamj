package fr.unice.polytech.equipe.j.order.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.equipe.j.FlexibleRestServer;
import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.order.dao.OrderDAO;
import fr.unice.polytech.equipe.j.order.dto.IndividualOrderDTO;
import fr.unice.polytech.equipe.j.order.dto.OrderDTO;
import fr.unice.polytech.equipe.j.order.dto.OrderStatus;
import fr.unice.polytech.equipe.j.order.dto.PaymentMethod;
import fr.unice.polytech.equipe.j.order.entities.IndividualOrderEntity;
import fr.unice.polytech.equipe.j.order.mapper.IndividualOrderMapper;
import fr.unice.polytech.equipe.j.order.mapper.OrderMapper;
import fr.unice.polytech.equipe.j.restaurant.dao.RestaurantDAO;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.restaurant.mapper.RestaurantMapper;
import fr.unice.polytech.equipe.j.user.dao.CampusUserDAO;
import fr.unice.polytech.equipe.j.user.dto.CampusUserDTO;
import fr.unice.polytech.equipe.j.user.mapper.CampusUserMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;

class OrderControllerTest {
    private static final UUID RESTAURANT_ID = UUID.fromString("3183fa1c-ecd5-49a9-9351-92f75d33fea4"); // TEST Restaurant
    private static final UUID ORDER_ID = UUID.fromString("178225f2-9f08-4a7f-add2-3783e89ffa6b"); // TEST Order
    private static final UUID USER_ID = UUID.fromString("1aeb4480-305a-499d-885c-7d2d9f99153b"); // TEST User

    // Start the server
    @BeforeAll
    public static void startServer() {
        // Start the server (assuming the server is already started, this is just an example)
        FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j", 5003);
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
        return new RestaurantDTO(RESTAURANT_ID, "TEST", null, null, new ArrayList<>(), menuDTO);
    }

    private OrderDTO getOrder() {
        RestaurantDAO.save(RestaurantMapper.toEntity(getRestaurant()));
        CampusUserDAO.save(CampusUserMapper.toEntity(getCampusUser()));

        CampusUserDTO userDTO = CampusUserMapper.toDTO(CampusUserDAO.getUserById(USER_ID));
        RestaurantDTO restaurantDTO = RestaurantMapper.toDTO(RestaurantDAO.getRestaurantById(RESTAURANT_ID));

        return new OrderDTO(ORDER_ID, restaurantDTO.getUuid(), userDTO.getId(), List.of(restaurantDTO.getMenu().getItems().getFirst()), OrderStatus.PENDING.name());
    }

    @Test
    void testGetAllOrders() throws Exception {
        OrderDAO.save(OrderMapper.toEntity(getOrder()));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5003/api/database/orders/all"))
                .GET()
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(200, response.statusCode());

        // Parse the response body into a list of CampusUserDTO objects.
        ObjectMapper objectMapper = new ObjectMapper();
        List<OrderDTO> orderDTOList = objectMapper.readValue(response.body(), new TypeReference<List<OrderDTO>>() {
        });

        // Assert that the user list is not null or empty.
        assertNotNull(orderDTOList);
        assertEquals(CampusUserDAO.getAll().size(), orderDTOList.size());
    }


    @Test
    void testCreateOrder() throws Exception {
        OrderDTO orderDTO = getOrder();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5003/api/database/orders/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(orderDTO)))
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        ObjectMapper objectMapper = new ObjectMapper();
        assertEquals(orderDTO.getId(), objectMapper.readValue(response.body(), UUID.class));
    }

    @Test
    void testGetOrderById() throws Exception {
        OrderDAO.save(OrderMapper.toEntity(getOrder()));
        // Create the HttpClient to send the request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5003/api/database/orders/" + ORDER_ID))
                .GET()
                .build();

        // Send the request and get the response
        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertNotNull(response.body());
        assertEquals(200, response.statusCode());

        // Parse the response body to OrderDTO
        ObjectMapper objectMapper = new ObjectMapper();
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
                .uri(URI.create("http://localhost:5003/api/database/orders/" + orderDTO.getId()))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(orderDTO)))
                .build();
        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        orderDTO = new ObjectMapper().readValue(response.body(), OrderDTO.class);
        assertEquals(orderDTO.getId(), orderDTO.getId());

        // Get the updated order by id and check status update
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5003/api/database/orders/" + orderDTO.getId()))
                .GET()
                .build();
        java.net.http.HttpResponse<String> getResponse = client.send(getRequest, java.net.http.HttpResponse.BodyHandlers.ofString());
        OrderDTO updatedOrderDTO = new ObjectMapper().readValue(getResponse.body(), OrderDTO.class);
        assertEquals(OrderStatus.VALIDATED.name(), updatedOrderDTO.getStatus());
        assertEquals(orderDTO.getId(), updatedOrderDTO.getId());
    }


    // Test the DELETE /{id} endpoint
    @Test
    void testDeleteOrder() throws Exception {
        OrderDTO orderDTO = getOrder();
        OrderDAO.save(OrderMapper.toEntity(orderDTO));
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5003/api/database/orders/" + orderDTO.getId()))
                .DELETE()
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

    @Test
    void testCreateIndividualOrder() throws Exception {
        // Create an IndividualOrderDTO
        IndividualOrderDTO individualOrderDTO = new IndividualOrderDTO();

        // Send request
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:5003/api/database/orders/individual/create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(new ObjectMapper().writeValueAsString(individualOrderDTO)))
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode());
    }

}
