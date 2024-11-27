package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.order.dao.OrderDAO;
import fr.unice.polytech.equipe.j.order.dto.OrderDTO;
import fr.unice.polytech.equipe.j.order.dto.OrderStatus;
import fr.unice.polytech.equipe.j.order.dto.PaymentMethod;
import fr.unice.polytech.equipe.j.order.mapper.OrderMapper;
import fr.unice.polytech.equipe.j.restaurant.dao.RestaurantDAO;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.restaurant.mapper.RestaurantMapper;
import fr.unice.polytech.equipe.j.user.dao.CampusUserDAO;
import fr.unice.polytech.equipe.j.user.dto.CampusUserDTO;
import fr.unice.polytech.equipe.j.user.mapper.CampusUserMapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderDatabaseSeeder {

    public static void seedDatabase() {
        // Creating CampusUser and Restaurant data
        CampusUserDTO userDTO = new CampusUserDTO();
        userDTO.setId(UUID.randomUUID());
        userDTO.setName("Jane Smith");
        userDTO.setBalance(50.0);
        userDTO.setTransactions(List.of());
        userDTO.setOrdersHistory(List.of());
        userDTO.setDefaultPaymentMethod(PaymentMethod.CREDIT_CARD);

        // Save CampusUser
        CampusUserDAO.save(CampusUserMapper.toEntity(userDTO));

        // Create a sample restaurant
        RestaurantDTO restaurantDTO = new RestaurantDTO(UUID.randomUUID(), "Le Petit Nice", null, null, new ArrayList<>(), new MenuDTO());
        restaurantDTO.getMenu().setUuid(UUID.randomUUID());
        restaurantDTO.getMenu().setItems(List.of(new MenuItemDTO(UUID.randomUUID(), "Salade Nicoise", 60, 12.50)));

        // Save Restaurant
        RestaurantDAO.save(RestaurantMapper.toEntity(restaurantDTO));

        // Creating Order for the user
        OrderDTO orderDTO = new OrderDTO(
                UUID.randomUUID(),
                restaurantDTO.getUuid(),
                userDTO.getId(),
                List.of(restaurantDTO.getMenu().getItems().get(0)),
                OrderStatus.PENDING.name()
        );

        // Save Order
        OrderDAO.save(OrderMapper.toEntity(orderDTO));

        // Repeat the process for more orders if needed
        createAnotherOrder();
    }

    private static void createAnotherOrder() {
        // Similar to above, create another order for a different user and restaurant
        CampusUserDTO userDTO = new CampusUserDTO();
        userDTO.setId(UUID.randomUUID());
        userDTO.setName("John Doe");
        userDTO.setBalance(150.0);
        userDTO.setTransactions(List.of());
        userDTO.setOrdersHistory(List.of());
        userDTO.setDefaultPaymentMethod(PaymentMethod.CREDIT_CARD);

        // Save user
        CampusUserDAO.save(CampusUserMapper.toEntity(userDTO));

        // Create Restaurant data
        RestaurantDTO restaurantDTO = new RestaurantDTO(UUID.randomUUID(), "Le Gourmet", null, null, new ArrayList<>(), new MenuDTO());
        restaurantDTO.getMenu().setUuid(UUID.randomUUID());
        restaurantDTO.getMenu().setItems(List.of(new MenuItemDTO(UUID.randomUUID(), "Magret de Canard", 300, 22.00)));

        // Save restaurant
        RestaurantDAO.save(RestaurantMapper.toEntity(restaurantDTO));

        // Creating another Order for the user
        OrderDTO orderDTO = new OrderDTO(
                UUID.randomUUID(),
                restaurantDTO.getUuid(),
                userDTO.getId(),
                List.of(restaurantDTO.getMenu().getItems().get(0)),
                OrderStatus.PENDING.name()
        );

        // Save Order
        OrderDAO.save(OrderMapper.toEntity(orderDTO));
    }

    public static void main(String[] args) {
        // Clear database and seed orders
        seedDatabase();

        // Show the database (or print the number of orders created)
        System.out.println("Orders in database: " + OrderDAO.getAllOrders().size());
    }
}
