package java.fr.unice.polytech.equipe.j.restaurant;

import java.fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.restaurant.backend.IRestaurant;
import fr.unice.polytech.equipe.j.restaurant.backend.Restaurant;
import fr.unice.polytech.equipe.j.restaurant.backend.RestaurantProxy;
import fr.unice.polytech.equipe.j.restaurant.backend.RestaurantServiceManager;
import java.fr.unice.polytech.equipe.j.restaurant.menu.Menu;
import java.fr.unice.polytech.equipe.j.restaurant.menu.MenuItem;
import java.fr.unice.polytech.equipe.j.user.RestaurantManager;
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
        TimeUtils.setClock(Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris")));
        RestaurantServiceManager manager = RestaurantServiceManager.getInstance();
        LocalDateTime fixedDateTime = TimeUtils.getNow();

        // add menu items to the restaurant
        List<MenuItem> itemsRestaurant1 = new ArrayList<>();
        itemsRestaurant1.add(new MenuItem("Salade Nicoise", 60, 12.50));
        itemsRestaurant1.add(new MenuItem("Bouillabaisse", 500, 25.00));
        itemsRestaurant1.add(new MenuItem("Tarte Tatin", 1800, 8.00));
        IRestaurant restaurant1 = new Restaurant("Le Petit Nice", fixedDateTime, fixedDateTime.plusHours(2), new Menu.MenuBuilder().addMenuItems(itemsRestaurant1).build());
        RestaurantManager restaurantManager = new RestaurantManager(
                "manager@test.com",
                "password",
                "Manager",
                restaurant1
        );
        restaurant1.setNumberOfPersonnel(restaurantManager.getRestaurant().getSlots().get(0), 5);
        restaurant1.setNumberOfPersonnel(restaurantManager.getRestaurant().getSlots().get(1), 10);
        restaurant1.setNumberOfPersonnel(restaurantManager.getRestaurant().getSlots().get(2), 3);
        restaurant1.setNumberOfPersonnel(restaurantManager.getRestaurant().getSlots().get(3), 2);

        List<MenuItem> itemsRestaurant2 = new ArrayList<>();
        itemsRestaurant2.add(new MenuItem("Salade de chèvre chaud", 400, 10.00));
        itemsRestaurant2.add(new MenuItem("Magret de canard", 1800, 20.00));
        itemsRestaurant2.add(new MenuItem("Crème brûlée", 600, 7.00));
        IRestaurant restaurant2 = new Restaurant("Le Petit Jardin", fixedDateTime, fixedDateTime.plusHours(2), new Menu.MenuBuilder().addMenuItems(itemsRestaurant2).build());
        RestaurantManager restaurantManager2 = new RestaurantManager(
                "manager2@test.com",
                "password",
                "Manager2",
                restaurant2
        );
        restaurant2.setNumberOfPersonnel(restaurantManager2.getRestaurant().getSlots().get(0), 5);
        restaurant2.setNumberOfPersonnel(restaurantManager2.getRestaurant().getSlots().get(1), 11);
        restaurant2.setNumberOfPersonnel(restaurantManager2.getRestaurant().getSlots().get(2), 3);
        restaurant2.setNumberOfPersonnel(restaurantManager2.getRestaurant().getSlots().get(3), 2);

        List<MenuItem> itemsRestaurant3 = new ArrayList<>();
        itemsRestaurant3.add(new MenuItem("Escargots", 1800, 15.00));
        itemsRestaurant3.add(new MenuItem("Coq au vin", 1800, 22.00));
        itemsRestaurant3.add(new MenuItem("Mousse au chocolat", 1800, 6.00));
        IRestaurant restaurant3 = new Restaurant("Le Petit Chateau", fixedDateTime, fixedDateTime.plusHours(2), new Menu.MenuBuilder().addMenuItems(itemsRestaurant3).build());
        RestaurantManager restaurantManager3 = new RestaurantManager(
                "manager3@test.com",
                "password",
                "Manager3",
                restaurant3
        );
        restaurant3.setNumberOfPersonnel(restaurantManager3.getRestaurant().getSlots().get(0), 5);
        restaurant3.setNumberOfPersonnel(restaurantManager3.getRestaurant().getSlots().get(1), 10);
        restaurant3.setNumberOfPersonnel(restaurantManager3.getRestaurant().getSlots().get(2), 3);
        restaurant3.setNumberOfPersonnel(restaurantManager3.getRestaurant().getSlots().get(3), 2);

        manager.addRestaurant(new RestaurantProxy(restaurant1));
        manager.addRestaurant(new RestaurantProxy(restaurant2));
        manager.addRestaurant(new RestaurantProxy(restaurant3));

        // Add a new restaurant that is currently closed
        List<MenuItem> itemsRestaurantA = new ArrayList<>();
        itemsRestaurantA.add(new MenuItem("Soupe à l'oignon", 300, 8.50));
        itemsRestaurantA.add(new MenuItem("Boeuf Bourguignon", 1500, 22.00));
        itemsRestaurantA.add(new MenuItem("Tarte Tatin", 500, 6.50));
        IRestaurant restaurantA = new Restaurant("Le Gourmet D'Or", null, null, new Menu.MenuBuilder().addMenuItems(itemsRestaurantA).build());
        manager.addRestaurant(new RestaurantProxy(restaurantA));

        // Add a new restaurant with no personnel
        List<MenuItem> itemsRestaurantB = new ArrayList<>();
        itemsRestaurantB.add(new MenuItem("Quiche Lorraine", 400, 9.00));
        itemsRestaurantB.add(new MenuItem("Ratatouille", 800, 12.00));
        itemsRestaurantB.add(new MenuItem("Mousse au chocolat", 350, 5.00));
        IRestaurant restaurantB = new Restaurant("Bistro de la Plage", fixedDateTime, fixedDateTime.plusHours(2), new Menu.MenuBuilder().addMenuItems(itemsRestaurantB).build());
        manager.addRestaurant(new RestaurantProxy(restaurantB));

        // Add a new restaurant with no menu
        IRestaurant restaurantC = new Restaurant("Café de l'Aube", fixedDateTime, fixedDateTime.plusHours(2), new Menu.MenuBuilder().build());
        RestaurantManager restaurantManagerC = new RestaurantManager(
                "managerC@email.com",
                "mypassword",
                "ManagerC",
                restaurantC
        );
        restaurantC.setNumberOfPersonnel(restaurantManagerC.getRestaurant().getSlots().get(0), 6);
        restaurantC.setNumberOfPersonnel(restaurantManagerC.getRestaurant().getSlots().get(1), 5);
        restaurantC.setNumberOfPersonnel(restaurantManagerC.getRestaurant().getSlots().get(2), 2);
        restaurantC.setNumberOfPersonnel(restaurantManagerC.getRestaurant().getSlots().get(3), 3);
        manager.addRestaurant(new RestaurantProxy(restaurantC));

        // Add a new restaurant with limited personnel but long preparation times
        List<MenuItem> itemsRestaurantD = new ArrayList<>();
        itemsRestaurantD.add(new MenuItem("Coq au Vin", 1200, 18.00));
        itemsRestaurantD.add(new MenuItem("Bouillabaisse", 1800, 25.00));
        itemsRestaurantD.add(new MenuItem("Crêpe Suzette", 400, 7.50));
        IRestaurant restaurantD = new Restaurant("La Table Royale", fixedDateTime, fixedDateTime.plusHours(1), new Menu.MenuBuilder().addMenuItems(itemsRestaurantD).build());
        RestaurantManager restaurantManagerD = new RestaurantManager(
                "managerD@email.com",
                "mypassword123",
                "ManagerD",
                restaurantD
        );
        restaurantD.setNumberOfPersonnel(restaurantManagerD.getRestaurant().getSlots().get(0), 2);
        restaurantD.setNumberOfPersonnel(restaurantManagerD.getRestaurant().getSlots().get(1), 1);
        manager.addRestaurant(new RestaurantProxy(restaurantD));
    }
}
