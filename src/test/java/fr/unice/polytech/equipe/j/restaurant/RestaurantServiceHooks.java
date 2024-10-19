package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.user.RestaurantManager;
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
        Clock clock = Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris"));
        RestaurantServiceManager manager = RestaurantServiceManager.getInstance(clock);
        LocalDateTime fixedDateTime = LocalDateTime.now(clock);

        // add menu items to the restaurant
        List<MenuItem> itemsRestaurant1 = new ArrayList<>();
        itemsRestaurant1.add(new MenuItem("Salade Nicoise", "lorem ipsum", 60, 12.50));
        itemsRestaurant1.add(new MenuItem("Bouillabaisse", "lorem ipsum", 500, 25.00));
        itemsRestaurant1.add(new MenuItem("Tarte Tatin", "lorem ipsum", 1900, 8.00));
        Restaurant restaurant1 = new Restaurant("Le Petit Nice", fixedDateTime, fixedDateTime.plusHours(2), new Menu(itemsRestaurant1), clock);
        RestaurantManager restaurantManager = new RestaurantManager(
                "manager@test.com",
                "password",
                "Manager",
                restaurant1
        );
        restaurantManager.updateNumberOfPersonnel(restaurantManager.getRestaurant().getSlots().get(0), 5);
        restaurantManager.updateNumberOfPersonnel(restaurantManager.getRestaurant().getSlots().get(1), 10);
        restaurantManager.updateNumberOfPersonnel(restaurantManager.getRestaurant().getSlots().get(2), 3);
        restaurantManager.updateNumberOfPersonnel(restaurantManager.getRestaurant().getSlots().get(3), 2);

        List<MenuItem> itemsRestaurant2 = new ArrayList<>();
        itemsRestaurant2.add(new MenuItem("Salade de chèvre chaud", "lorem ipsum", 400, 10.00));
        itemsRestaurant2.add(new MenuItem("Magret de canard", "lorem ipsum", 2000, 20.00));
        itemsRestaurant2.add(new MenuItem("Crème brûlée", "lorem ipsum", 600, 7.00));
        Restaurant restaurant2 = new Restaurant("Le Petit Jardin", fixedDateTime, fixedDateTime.plusHours(2), new Menu(itemsRestaurant2), clock);
        RestaurantManager restaurantManager2 = new RestaurantManager(
                "manager2@test.com",
                "password",
                "Manager2",
                restaurant2
        );
        restaurantManager2.updateNumberOfPersonnel(restaurantManager2.getRestaurant().getSlots().get(0), 5);
        restaurantManager2.updateNumberOfPersonnel(restaurantManager2.getRestaurant().getSlots().get(1), 10);
        restaurantManager2.updateNumberOfPersonnel(restaurantManager2.getRestaurant().getSlots().get(2), 3);
        restaurantManager2.updateNumberOfPersonnel(restaurantManager2.getRestaurant().getSlots().get(3), 2);


        List<MenuItem> itemsRestaurant3 = new ArrayList<>();
        itemsRestaurant3.add(new MenuItem("Escargots", "lorem ipsum", 1900, 15.00));
        itemsRestaurant3.add(new MenuItem("Coq au vin", "lorem ipsum", 1840, 22.00));
        itemsRestaurant3.add(new MenuItem("Mousse au chocolat", "lorem ipsum", 1800, 6.00));
        Restaurant restaurant3 = new Restaurant("Le Petit Chateau", fixedDateTime, fixedDateTime.plusHours(2), new Menu(itemsRestaurant3), clock);
        RestaurantManager restaurantManager3 = new RestaurantManager(
                "manager3@test.com",
                "password",
                "Manager3",
                restaurant3
        );
        restaurantManager3.updateNumberOfPersonnel(restaurantManager3.getRestaurant().getSlots().get(0), 5);
        restaurantManager3.updateNumberOfPersonnel(restaurantManager3.getRestaurant().getSlots().get(1), 10);
        restaurantManager3.updateNumberOfPersonnel(restaurantManager3.getRestaurant().getSlots().get(2), 3);
        restaurantManager3.updateNumberOfPersonnel(restaurantManager3.getRestaurant().getSlots().get(3), 2);


        manager.addRestaurant(restaurant1);
        manager.addRestaurant(restaurant2);
        manager.addRestaurant(restaurant3);
    }
}
