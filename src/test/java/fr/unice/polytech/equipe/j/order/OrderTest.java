package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.restaurant.IRestaurant;
import fr.unice.polytech.equipe.j.restaurant.menu.Menu;
import fr.unice.polytech.equipe.j.restaurant.menu.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.restaurant.RestaurantProxy;
import fr.unice.polytech.equipe.j.user.CampusUser;
import fr.unice.polytech.equipe.j.user.RestaurantManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    private Order order;
    private IRestaurant restaurant;

    @BeforeEach
    void setUp() {
        TimeUtils.setClock(Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris")));
        LocalDateTime fixedDateTime = TimeUtils.getNow();

        List<MenuItem> itemsRestaurant1 = new ArrayList<>();
        itemsRestaurant1.add(new MenuItem("Salade Nicoise", 60, 12.50));
        itemsRestaurant1.add(new MenuItem("Bouillabaisse", 500, 25.00));
        itemsRestaurant1.add(new MenuItem("Tarte Tatin", 1800, 8.00));
        restaurant = new RestaurantProxy(new Restaurant("Le Petit Nice", fixedDateTime, fixedDateTime.plusHours(2), new Menu.MenuBuilder().addMenuItems(itemsRestaurant1).build()));
        RestaurantManager restaurantManager = new RestaurantManager(
                "manager@test.com",
                "password",
                "Manager",
                restaurant
        );
        restaurantManager.updateNumberOfPersonnel(restaurantManager.getRestaurant().getSlots().get(0), 5);
        order = new Order(restaurant, new CampusUser("John", 0));
    }

    @Test
    void testAddItem() {
        order.addItem(restaurant.getMenu().getItems().getFirst());
        assertEquals(1, order.getItems().size());
    }

    @Test
    void testRemoveItem() {
        MenuItem menuItem = restaurant.getMenu().getItems().get(1);
        order.addItem(menuItem);
        order.removeItem(menuItem);
        assertEquals(0, order.getItems().size());
    }

    @Test
    void testSetStatus() {
        order.setStatus(OrderStatus.VALIDATED);
        assertEquals(OrderStatus.VALIDATED, order.getStatus());
    }

    @Test
    void testCannotAddItemAfterValidated() {
        order.setStatus(OrderStatus.VALIDATED);
        MenuItem menuItem = new MenuItem("Pizza", 50, 10.0);
        assertThrows(IllegalArgumentException.class, () -> order.addItem(menuItem));
    }
}
