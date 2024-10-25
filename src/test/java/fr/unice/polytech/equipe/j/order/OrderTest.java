package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.restaurant.RestaurantServiceManager;
import fr.unice.polytech.equipe.j.user.CampusUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    private Order order;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        TimeUtils.setClock(Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris")));
        // Mocking Restaurant
        restaurant = RestaurantServiceManager.getInstance().getRestaurants().getFirst();
        order = new Order(restaurant, new CampusUser("email", "psw", new OrderManager()));
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
        assertThrows(IllegalStateException.class, () -> order.addItem(menuItem));
    }
}
