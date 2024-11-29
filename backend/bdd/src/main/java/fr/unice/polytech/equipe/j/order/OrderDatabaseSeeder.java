package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.order.dao.OrderDAO;
import fr.unice.polytech.equipe.j.order.dto.OrderDTO;
import fr.unice.polytech.equipe.j.order.dto.OrderStatus;
import fr.unice.polytech.equipe.j.order.entities.OrderEntity;
import fr.unice.polytech.equipe.j.order.mapper.OrderMapper;
import fr.unice.polytech.equipe.j.restaurant.dao.RestaurantDAO;
import fr.unice.polytech.equipe.j.restaurant.dto.MenuItemDTO;
import fr.unice.polytech.equipe.j.restaurant.dto.RestaurantDTO;
import fr.unice.polytech.equipe.j.restaurant.entities.RestaurantEntity;
import fr.unice.polytech.equipe.j.restaurant.mapper.RestaurantMapper;
import fr.unice.polytech.equipe.j.user.dao.CampusUserDAO;
import fr.unice.polytech.equipe.j.user.dto.CampusUserDTO;
import fr.unice.polytech.equipe.j.user.entities.CampusUserEntity;
import fr.unice.polytech.equipe.j.user.mapper.CampusUserMapper;

import java.util.List;
import java.util.UUID;

public class OrderDatabaseSeeder {

    public static void seedDatabase() {
        // Fetch user
        CampusUserEntity userEntity = CampusUserDAO.getUserById(UUID.fromString("2ed64a86-d499-4a9c-a0a1-9aba06297348"));
        if (userEntity == null) {
            System.out.println("User not found!");
            return;
        }
        CampusUserDTO userDTO = CampusUserMapper.toDTO(userEntity);

        // Fetch restaurant
        RestaurantEntity restaurantEntity = RestaurantDAO.getRestaurantById(UUID.fromString("0e1e323a-ac8c-4833-94cf-1f14aac83285"));
        if (restaurantEntity == null) {
            System.out.println("Restaurant not found!");
            return;
        }
        RestaurantDTO restaurantDTO = RestaurantMapper.toDTO(restaurantEntity);

        // Check if the restaurant has a menu with items
        if (restaurantDTO.getMenu() == null || restaurantDTO.getMenu().getItems().isEmpty()) {
            System.out.println("Restaurant menu is empty!");
            return;
        }
        MenuItemDTO menuItemDTO = restaurantDTO.getMenu().getItems().getFirst();

        // Create OrderDTO
        OrderDTO orderDTO = new OrderDTO(
                UUID.fromString("581066b1-074a-4cf9-92f4-32b271fb53be"),
                restaurantDTO.getUuid(),
                userDTO.getId(),
                List.of(menuItemDTO),
                OrderStatus.PENDING.name()
        );
        // Convert to OrderEntity
        OrderEntity orderEntity = OrderMapper.toEntity(orderDTO);

        System.out.println("Saving order..." + orderEntity.getRestaurantId());

        if (OrderDAO.save(orderEntity).getCode() == 201) {
            System.out.println("Order saved successfully!");
            return;
        }
        System.out.println("Error saving order");
    }


    public static void main(String[] args) {
        // Clear database and seed orders
        seedDatabase();

        // Show the database (or print the number of orders created)
        System.out.println("Orders in database: " + OrderDAO.getAllOrders().size());
    }
}
