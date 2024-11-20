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
        CampusUserDTO campusUserDTO = new CampusUserDTO(UUID.fromString("2ed64a86-d499-4a9c-a0a1-9aba06297348"),
                "John",
                0.0,
                PaymentMethod.CREDIT_CARD,
                new ArrayList<>(),
                new ArrayList<>());
        CampusUserDAO.save(CampusUserMapper.toEntity(campusUserDTO));

        // Campus User 2
        campusUserDTO = new CampusUserDTO(UUID.fromString("1aeb4480-305a-499d-885c-7d2d9f99153b"),
                "Jane",
                0.0,
                PaymentMethod.CREDIT_CARD,
                new ArrayList<>(),
                new ArrayList<>());
        CampusUserDAO.save(CampusUserMapper.toEntity(campusUserDTO));

        // Restaurant Manager 1
        RestaurantManagerDTO restaurantManagerDTO = new RestaurantManagerDTO(UUID.fromString("23a114ce-8d23-4624-a962-14658208b755"),
                "bob@email.com",
                "Bob",
                RestaurantMapper.toDTO(RestaurantDAO.getAllRestaurants().getFirst()));
        RestaurantManagerDAO.save(RestaurantManagerMapper.toEntity(restaurantManagerDTO));

        // Restaurant Manager 2
        restaurantManagerDTO = new RestaurantManagerDTO(UUID.fromString("d49de281-47c0-43ef-91b1-1f1a717363b4"),
                "alice@email.com",
                "Alice",
                RestaurantMapper.toDTO(RestaurantDAO.getAllRestaurants().getLast()));
        RestaurantManagerDAO.save(RestaurantManagerMapper.toEntity(restaurantManagerDTO));
    }

    public static void main(String[] args) {
        seedDatabase();

        System.out.println(CampusUserDAO.getAll().size());
        System.out.println(RestaurantManagerDAO.getAll().size());
    }
}
