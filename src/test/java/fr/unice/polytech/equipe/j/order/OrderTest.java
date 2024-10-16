package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    private Order order;
    private Restaurant restaurant;

    @BeforeEach
    void setUp() {
        // Mocking RestaurantFacade
        restaurant = Mockito.mock(Restaurant.class);
        order = new Order(restaurant);
    }

    @Test
    void testAddItem() {
        MenuItem menuItem = new MenuItem("Pizza", 10.0);
        order.addItem(menuItem);
        assertEquals(1, order.getItems().size());
    }

    @Test
    void testRemoveItem() {
        MenuItem menuItem = new MenuItem("Pizza", 10.0);
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
        MenuItem menuItem = new MenuItem("Pizza", 10.0);
        assertThrows(IllegalStateException.class, () -> order.addItem(menuItem));
    }
}
