package fr.unice.polytech.equipe.j.tests.order;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

class OrderTest {

    private Order order;
    private Restaurant restaurant;
    private MenuItem item1;

    @BeforeEach
    void setUp() {
        // Mock the Restaurant class
        restaurant = mock(Restaurant.class);

        // Create an Order object with the mocked Restaurant
        order = new Order(restaurant);

        // Create some menu items
        item1 = new MenuItem("Burger", 5.99);
    }

    @Test
    void testAddItem() {
        order.addItem(item1);
        assertTrue(order.getItems().contains(item1));
        assertEquals(1, order.getItems().size());
    }

    @Test
    void testRemoveItem() {
        order.addItem(item1);
        order.removeItem(item1);
        assertFalse(order.getItems().contains(item1));
    }

    @Test
    void testOrderIdIsGenerated() {
        assertNotNull(order.getOrderUUID());
    }
}
