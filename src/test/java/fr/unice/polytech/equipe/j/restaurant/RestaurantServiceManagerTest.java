package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.Order;
import io.cucumber.java.bs.A;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

class RestaurantServiceManagerTest {
    private RestaurantServiceManager restaurantServiceManager;
    private Clock clock;

    @BeforeEach
    void setUp() {
        RestaurantServiceHooks.setUp();
        clock = Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris"));
        restaurantServiceManager = RestaurantServiceManager.getInstance(clock);
    }

    @Test
    void noDuplicateRestaurant() {
        RestaurantServiceManager manager = RestaurantServiceManager.getInstance(clock);
        List<Restaurant> restaurants = manager.searchByName("Le Petit Nice");
        Assertions.assertEquals(1, restaurants.size());
    }

    @Test
    void noDuplicationGetRestaurantByFood() {
        RestaurantServiceManager manager = RestaurantServiceManager.getInstance(clock);
        List<Restaurant> restaurants = manager.searchByTypeOfFood("Salade");
        Assertions.assertEquals(2, restaurants.size());
    }

    @Test
    void testAddRestaurant() {
        int initialSize = restaurantServiceManager.getRestaurants().size();
        Restaurant restaurant = new Restaurant("Test Restaurant", new ArrayList<>(), LocalDateTime.now(clock), LocalDateTime.now(clock).plusHours(8), null, clock);
        restaurantServiceManager.addRestaurant(restaurant);
        List<Restaurant> restaurants = restaurantServiceManager.getRestaurants();
        Assertions.assertEquals(initialSize + 1, restaurants.size());
    }

    @Test
    void testRemoveRestaurant() {
        int initialSize = restaurantServiceManager.getRestaurants().size();
        Restaurant restaurant = RestaurantServiceManager.getInstance(clock).getRestaurants().getFirst();
        restaurantServiceManager.addRestaurant(restaurant);
        restaurantServiceManager.removeRestaurant(restaurant);
        List<Restaurant> restaurants = restaurantServiceManager.getRestaurants();
        if (initialSize > 0) {
            Assertions.assertEquals(initialSize, restaurants.size());
        } else {
            Assertions.assertEquals(initialSize - 1, restaurants.size());
        }
    }
}
