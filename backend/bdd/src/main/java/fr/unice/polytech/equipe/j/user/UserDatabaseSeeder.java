package fr.unice.polytech.equipe.j.user;

import fr.unice.polytech.equipe.j.order.dto.PaymentMethod;
import fr.unice.polytech.equipe.j.restaurant.dao.RestaurantDAO;
import fr.unice.polytech.equipe.j.restaurant.mapper.RestaurantMapper;
import fr.unice.polytech.equipe.j.user.dao.CampusUserDAO;
import fr.unice.polytech.equipe.j.user.dao.RestaurantManagerDAO;
import fr.unice.polytech.equipe.j.user.dto.CampusUserDTO;
import fr.unice.polytech.equipe.j.user.dto.RestaurantManagerDTO;
import fr.unice.polytech.equipe.j.user.mapper.CampusUserMapper;
import fr.unice.polytech.equipe.j.user.mapper.RestaurantManagerMapper;

import java.util.ArrayList;
import java.util.UUID;

public class UserDatabaseSeeder {

    public static void seedDatabase() {
        // Campus User 1
        CampusUserDTO campusUserDTO = new CampusUserDTO(UUID.randomUUID(),
                "John",
                0.0,
                PaymentMethod.CREDIT_CARD,
                new ArrayList<>(),
                new ArrayList<>());
        CampusUserDAO.save(CampusUserMapper.toEntity(campusUserDTO));

        // Campus User 2
        campusUserDTO = new CampusUserDTO(UUID.randomUUID(),
                "Jane",
                0.0,
                PaymentMethod.CREDIT_CARD,
                new ArrayList<>(),
                new ArrayList<>());
        CampusUserDAO.save(CampusUserMapper.toEntity(campusUserDTO));

        // Restaurant Manager 1
        RestaurantManagerDTO restaurantManagerDTO = new RestaurantManagerDTO(UUID.randomUUID(),
                "bob@email.com",
                "Bob",
                RestaurantMapper.toDTO(RestaurantDAO.getAllRestaurants().getFirst()));
        RestaurantManagerDAO.save(RestaurantManagerMapper.toEntity(restaurantManagerDTO));

        // Restaurant Manager 2
        restaurantManagerDTO = new RestaurantManagerDTO(UUID.randomUUID(),
                "alice@email.com",
                "Alice",
                RestaurantMapper.toDTO(RestaurantDAO.getAllRestaurants().getLast()));
        RestaurantManagerDAO.save(RestaurantManagerMapper.toEntity(restaurantManagerDTO));
    }
}
