package fr.unice.polytech.equipe.j.restaurant;

import io.cucumber.java.Before;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class RestaurantServiceHooks {

    @Before
    public static void setUp() {
        RestaurantServiceManager.resetInstance();
        // Initialize the singleton
        RestaurantServiceManager manager = RestaurantServiceManager.getInstance();

        // add menu items to the restaurant
        List<MenuItem> itemsRestaurant1 = new ArrayList<>();
        itemsRestaurant1.add(new MenuItem("Salade Nicoise", 12.50));
        itemsRestaurant1.add(new MenuItem("Bouillabaisse", 25.00));
        itemsRestaurant1.add(new MenuItem("Tarte Tatin", 8.00));
        // Add some restaurants if necessary
        Restaurant restaurant1 = new Restaurant("Le Petit Nice", LocalDateTime.of(2021, 1, 1, 10, 0),
                LocalDateTime.of(2021, 1, 1, 22, 0), new Menu(itemsRestaurant1));

        List<MenuItem> itemsRestaurant2 = new ArrayList<>();
        itemsRestaurant2.add(new MenuItem("Salade de chèvre chaud", 10.00));
        itemsRestaurant2.add(new MenuItem("Magret de canard", 20.00));
        itemsRestaurant2.add(new MenuItem("Crème brûlée", 7.00));

        Restaurant restaurant2 = new Restaurant("Le Petit Jardin", LocalDateTime.of(2021, 1, 1, 11, 0),
                LocalDateTime.of(2021, 1, 1, 23, 0), new Menu(itemsRestaurant2));

        List<MenuItem> itemsRestaurant3 = new ArrayList<>();
        itemsRestaurant3.add(new MenuItem("Escargots", 15.00));
        itemsRestaurant3.add(new MenuItem("Coq au vin", 22.00));
        itemsRestaurant3.add(new MenuItem("Mousse au chocolat", 6.00));

        Restaurant restaurant3 = new Restaurant("Le Petit Chateau", LocalDateTime.of(2021, 1, 1, 9, 0),
                LocalDateTime.of(2021, 1, 1, 21, 0), new Menu(itemsRestaurant3));
        manager.addRestaurant(restaurant1);
        manager.addRestaurant(restaurant2);
        manager.addRestaurant(restaurant3);
    }
}
