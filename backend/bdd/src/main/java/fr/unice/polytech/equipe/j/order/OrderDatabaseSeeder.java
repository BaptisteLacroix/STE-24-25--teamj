package fr.unice.polytech.equipe.j.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
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
import fr.unice.polytech.equipe.j.restaurant.dao.RestaurantDAO;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.restaurant.mapper.RestaurantMapper;
import fr.unice.polytech.equipe.j.user.dao.CampusUserDAO;
import fr.unice.polytech.equipe.j.user.dto.CampusUserDTO;
import fr.unice.polytech.equipe.j.user.mapper.CampusUserMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static fr.unice.polytech.equipe.j.JacksonConfig.configureObjectMapper;

public class OrderDatabaseSeeder {

    private static final UUID RESTAURANT_ID = UUID.fromString("3183fa1c-ecd5-49a9-9351-92f75d33fea4"); // TEST Restaurant
    private static final UUID ORDER_ID = UUID.fromString("178225f2-9f08-4a7f-add2-3783e89ffa6b"); // TEST Order
    private static final UUID INDIVIDUAL_ID = UUID.fromString("f78ed5e2-face-43c3-a60e-7f703bd995c3"); // TEST Order
    private static final UUID USER_ID = UUID.fromString("1aeb4480-305a-499d-885c-7d2d9f99153b"); // TEST User
    private static final UUID DELIVERY_ID = UUID.fromString("774fcc38-ff40-4cc8-8722-a20bca38338d"); // TEST Delivery
    private static final UUID LOCATION_ID = UUID.fromString("10a83413-1b4a-4184-b082-238d073e6126"); // TEST Location
    private static final String ORDER_PATH = "http://localhost:5004/api/database/orders/";
    private static final String RESTAURANT_PATH = "http://localhost:5004/api/database/restaurants/";
    private static final String CAMPUS_USER_PATH = "http://localhost:5004/api/database/campusUsers/";

    private void getCampusUser() throws JsonProcessingException {
        CampusUserDTO newUser = new CampusUserDTO();
        newUser.setName("John Doe");
        newUser.setId(USER_ID);
        newUser.setBalance(100.0);
        newUser.setTransactions(List.of());
        newUser.setOrdersHistory(List.of());
        newUser.setDefaultPaymentMethod(PaymentMethod.CREDIT_CARD);
        ObjectMapper objectMapper = configureObjectMapper();
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(CAMPUS_USER_PATH + "create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(newUser)))
                .build();

        try {
            java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 201) {
                System.out.println("Error while populating the database! [CampusUser]");
            } else {
                System.out.println("Database populated with test values! [CampusUser]");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        // Get the user by id
        System.out.println("USER CREATED: " + CampusUserDAO.getUserById(USER_ID));
    }

    private void getRestaurant() throws IOException, InterruptedException {
        List<MenuItemDTO> itemsRestaurant = List.of(
                new MenuItemDTO(UUID.randomUUID(), "TEST", 0, 12.50),
                new MenuItemDTO(UUID.randomUUID(), "TEST", 1, 25.00),
                new MenuItemDTO(UUID.randomUUID(), "TEST", 2, 8.00)
        );
        MenuDTO menuDTO = new MenuDTO();
        menuDTO.setUuid(UUID.randomUUID());
        menuDTO.setItems(itemsRestaurant);
        RestaurantDTO restaurantDTO = new RestaurantDTO(RESTAURANT_ID, "TEST", null, null, new ArrayList<>(), menuDTO);

        HttpClient client = HttpClient.newHttpClient();
        ObjectWriter ow = JacksonConfig.configureObjectMapper().writer().withDefaultPrettyPrinter();
        String json = ow.writeValueAsString(restaurantDTO);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(RESTAURANT_PATH + "create"))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .build();

        java.net.http.HttpResponse<String> response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() != 201) {
            System.out.println("Error while populating the database! [Restaurant]");
        } else {
            System.out.println("Database populated with test values! [Restaurant]");
        }

        // Get the restaurant by id
        System.out.println("RESTAURANT CREATED: " + RestaurantDAO.getRestaurantById(RESTAURANT_ID));
    }

    private OrderDTO getOrder() throws IOException, InterruptedException {
        getRestaurant();
        getCampusUser();

        CampusUserDTO userDTO = CampusUserMapper.toDTO(CampusUserDAO.getUserById(USER_ID));
        RestaurantDTO restaurantDTO = RestaurantMapper.toDTO(RestaurantDAO.getRestaurantById(RESTAURANT_ID));

        return new OrderDTO(ORDER_ID, restaurantDTO.getId(), userDTO.getId(), List.of(restaurantDTO.getMenu().getItems().getFirst()), OrderStatus.PENDING.name());
    }

    private DeliveryDetailsDTO getDeliveryDetails() {
        DeliveryLocationDTO deliveryLocationDTO = new DeliveryLocationDTO(LOCATION_ID,
                "Campus Main Gate",
                "123 Campus Street");
        DeliveryLocationDAO.save(DeliveryLocationMapper.toEntity(deliveryLocationDTO));

        // Get the delivery location by id
        System.out.println("DELIVERY LOCATION CREATED: " + DeliveryLocationDAO.getDeliveryLocationById(LOCATION_ID));
        return new DeliveryDetailsDTO(DELIVERY_ID, deliveryLocationDTO, LocalDateTime.parse("2024-12-31T11:00:00"));
    }

    private IndividualOrderDTO getIndividualOrder() throws IOException, InterruptedException {
        getRestaurant();
        getCampusUser();

        CampusUserDTO userDTO = CampusUserMapper.toDTO(CampusUserDAO.getUserById(USER_ID));
        RestaurantDTO restaurantDTO = RestaurantMapper.toDTO(RestaurantDAO.getRestaurantById(RESTAURANT_ID));

        return new IndividualOrderDTO(new OrderDTO(INDIVIDUAL_ID, restaurantDTO.getId(), userDTO.getId(), List.of(restaurantDTO.getMenu().getItems().getFirst()), OrderStatus.PENDING.name()), getDeliveryDetails());
    }

    public static void seedDatabase() {

    }

    // Main method to populate the database
    public static void main(String[] args) {
        try {
            final FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j", 5004);
            server.start();
            OrderDatabaseSeeder orderDatabaseSeeder = new OrderDatabaseSeeder();
            HttpClient client;
            HttpRequest request;
            java.net.http.HttpResponse<String> response;
            IndividualOrderDTO individualOrderDTO = new IndividualOrderDTO(orderDatabaseSeeder.getIndividualOrder(), orderDatabaseSeeder.getDeliveryDetails());

            ObjectMapper objectMapper = configureObjectMapper();

            // Send request
            client = HttpClient.newHttpClient();
            request = HttpRequest.newBuilder()
                    .uri(URI.create(ORDER_PATH + "individual/create"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(objectMapper.writeValueAsString(individualOrderDTO)))
                    .build();

            response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println("Error while populating the database!");
            } else {
                System.out.println("Database populated with test values!");
            }
            System.out.println("Individual Orders:");
            OrderDAO.getAllOrders().forEach(System.out::println);
            System.out.println("End of Individual Orders");


            OrderDTO orderDTO = orderDatabaseSeeder.getOrder();
            client = HttpClient.newHttpClient();
            request = HttpRequest.newBuilder()
                    .uri(URI.create(ORDER_PATH + "create"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(JacksonConfig.configureObjectMapper().writeValueAsString(orderDTO)))
                    .build();

            response = client.send(request, java.net.http.HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 201) {
                System.out.println("Error while populating the database!");
            } else {
                System.out.println("Database populated with test values!");
            }
            System.out.println("Orders:");
            OrderDAO.getAllOrders().forEach(System.out::println);
            System.out.println("End of Orders");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
}

