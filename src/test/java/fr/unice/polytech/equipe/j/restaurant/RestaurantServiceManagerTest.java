package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.restaurant.backend.IRestaurant;
import fr.unice.polytech.equipe.j.restaurant.backend.Restaurant;
import fr.unice.polytech.equipe.j.restaurant.backend.RestaurantProxy;
import fr.unice.polytech.equipe.j.restaurant.backend.RestaurantServiceManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

class RestaurantServiceManagerTest {
    private RestaurantServiceManager restaurantServiceManager;

    @BeforeEach
    void setUp() {
        RestaurantServiceHooks.setUp();
        TimeUtils.setClock(Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris")));
        restaurantServiceManager = RestaurantServiceManager.getInstance();
    }

    @Test
    void noDuplicateRestaurant() {
        RestaurantServiceManager manager = RestaurantServiceManager.getInstance();
        List<IRestaurant> restaurants = manager.searchByName("Le Petit Nice");
        Assertions.assertEquals(1, restaurants.size());
    }

    @Test
    void noDuplicationGetRestaurantByFood() {
        RestaurantServiceManager manager = RestaurantServiceManager.getInstance();
        List<IRestaurant> restaurants = manager.searchByTypeOfFood("Salade");
        Assertions.assertEquals(2, restaurants.size());
    }

    @Test
    void testAddRestaurant() {
        int initialSize = restaurantServiceManager.getRestaurants().size();
        IRestaurant restaurant = new RestaurantProxy(new Restaurant("Test Restaurant", TimeUtils.getNow(), TimeUtils.getNow().plusHours(8), null));
        restaurantServiceManager.addRestaurant(restaurant);
        List<IRestaurant> restaurants = restaurantServiceManager.getRestaurants();
        Assertions.assertEquals(initialSize + 1, restaurants.size());
    }

    @Test
    void testRemoveRestaurant() {
        int initialSize = restaurantServiceManager.getRestaurants().size();
        IRestaurant restaurant = RestaurantServiceManager.getInstance().getRestaurants().getFirst();
        restaurantServiceManager.addRestaurant(restaurant);
        restaurantServiceManager.removeRestaurant(restaurant);
        List<IRestaurant> restaurants = restaurantServiceManager.getRestaurants();
        if (initialSize > 0) {
            Assertions.assertEquals(initialSize, restaurants.size());
        } else {
            Assertions.assertEquals(initialSize - 1, restaurants.size());
        }
    }
}
