package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.slot.Slot;
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

        List<Slot> slots = new ArrayList<>();

        // add menu items to the restaurant
        List<MenuItem> itemsRestaurant1 = new ArrayList<>();
        itemsRestaurant1.add(new MenuItem("Salade Nicoise", "lorem ipsum",10, 12.50, 8));
        itemsRestaurant1.add(new MenuItem("Bouillabaisse", "lorem ipsum",10, 25.00, 8));
        itemsRestaurant1.add(new MenuItem("Tarte Tatin", "lorem ipsum",10, 8.00, 8));
        // Add some restaurants if necessary
        Restaurant restaurant1 = new Restaurant("Le Petit Nice", slots, LocalDateTime.of(2021, 1, 1, 10, 0),
                LocalDateTime.of(2021, 1, 1, 22, 0), new Menu(itemsRestaurant1));

        List<MenuItem> itemsRestaurant2 = new ArrayList<>();
        itemsRestaurant2.add(new MenuItem("Salade de chèvre chaud", "lorem ipsum",10, 10.00, 8));
        itemsRestaurant2.add(new MenuItem("Magret de canard", "lorem ipsum",10, 20.00, 8));
        itemsRestaurant2.add(new MenuItem("Crème brûlée", "lorem ipsum",10, 7.00, 8));

        slots = new ArrayList<>();

        Restaurant restaurant2 = new Restaurant("Le Petit Jardin", slots, LocalDateTime.of(2021, 1, 1, 11, 0),
                LocalDateTime.of(2021, 1, 1, 23, 0), new Menu(itemsRestaurant2));

        List<MenuItem> itemsRestaurant3 = new ArrayList<>();
        itemsRestaurant3.add(new MenuItem("Escargots", "lorem ipsum",10, 15.00, 8));
        itemsRestaurant3.add(new MenuItem("Coq au vin", "lorem ipsum",10, 22.00, 8));
        itemsRestaurant3.add(new MenuItem("Mousse au chocolat", "lorem ipsum",10, 6.00, 8));

        slots = new ArrayList<>();

        Restaurant restaurant3 = new Restaurant("Le Petit Chateau", slots, LocalDateTime.of(2021, 1, 1, 9, 0),
                LocalDateTime.of(2021, 1, 1, 21, 0), new Menu(itemsRestaurant3));
        manager.addRestaurant(restaurant1);
        manager.addRestaurant(restaurant2);
        manager.addRestaurant(restaurant3);
    }
}
