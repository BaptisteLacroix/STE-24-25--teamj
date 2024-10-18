package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.slot.Slot;
import io.cucumber.java.Before;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

public class RestaurantServiceHooks {

    @Before
    public static void setUp() {
        RestaurantServiceManager.resetInstance();
        // Initialize the singleton
        Clock clock = Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.systemDefault());
        RestaurantServiceManager manager = RestaurantServiceManager.getInstance(clock);
        LocalDateTime fixedDateTime = LocalDateTime.now(clock);

        List<Slot> slots = new ArrayList<>();

        // add menu items to the restaurant
        List<MenuItem> itemsRestaurant1 = new ArrayList<>();
        itemsRestaurant1.add(new MenuItem("Salade Nicoise", "lorem ipsum", 60, 12.50));
        itemsRestaurant1.add(new MenuItem("Bouillabaisse", "lorem ipsum", 500, 25.00));
        itemsRestaurant1.add(new MenuItem("Tarte Tatin", "lorem ipsum", 1900, 8.00));
        // Add some restaurants if necessary
        Restaurant restaurant1 = new Restaurant("Le Petit Nice", slots, fixedDateTime, fixedDateTime.plusHours(2), new Menu(itemsRestaurant1), clock);

        List<MenuItem> itemsRestaurant2 = new ArrayList<>();
        itemsRestaurant2.add(new MenuItem("Salade de chèvre chaud", "lorem ipsum", 400, 10.00));
        itemsRestaurant2.add(new MenuItem("Magret de canard", "lorem ipsum", 2000, 20.00));
        itemsRestaurant2.add(new MenuItem("Crème brûlée", "lorem ipsum", 600, 7.00));

        slots = new ArrayList<>();

        Restaurant restaurant2 = new Restaurant("Le Petit Jardin", slots, fixedDateTime, fixedDateTime.plusHours(2), new Menu(itemsRestaurant2), clock);

        List<MenuItem> itemsRestaurant3 = new ArrayList<>();
        itemsRestaurant3.add(new MenuItem("Escargots", "lorem ipsum", 1900, 15.00));
        itemsRestaurant3.add(new MenuItem("Coq au vin", "lorem ipsum", 1840, 22.00));
        itemsRestaurant3.add(new MenuItem("Mousse au chocolat", "lorem ipsum", 1800, 6.00));

        slots = new ArrayList<>();

        Restaurant restaurant3 = new Restaurant("Le Petit Chateau", slots, fixedDateTime, fixedDateTime.plusHours(2), new Menu(itemsRestaurant3), clock);
        manager.addRestaurant(restaurant1);
        manager.addRestaurant(restaurant2);
        manager.addRestaurant(restaurant3);
    }
}
