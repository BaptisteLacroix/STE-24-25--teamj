package fr.unice.polytech.equipe.j.restaurant;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class RestaurantServiceManagerTest {

    @BeforeAll
    public static void setUp() {
        RestaurantServiceHooks.setUp();
    }

    @Test
    void noDuplicateRestaurant() {
        RestaurantServiceManager manager = RestaurantServiceManager.getInstance();
        List<RestaurantFacade> restaurants = manager.searchByName("Le Petit Nice");
        assertEquals(1, restaurants.size());
    }

    @Test
    void noDuplicationGetRestaurantByFood() {
        RestaurantServiceManager manager = RestaurantServiceManager.getInstance();
        List<RestaurantFacade> restaurants = manager.searchByTypeOfFood("Salade");
        assertEquals(2, restaurants.size());
    }
}
