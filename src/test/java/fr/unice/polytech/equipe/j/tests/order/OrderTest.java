package fr.unice.polytech.equipe.j.tests.order;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderTest {

    private Order order;
    private Restaurant restaurant;
    private MenuItem item1;
    private MenuItem item2;
    private UUID orderId = UUID.randomUUID();

    @BeforeEach
    void setUp() {
        // Mock the Restaurant class
        restaurant = mock(Restaurant.class);

        // Create an Order object with the mocked Restaurant
        order = new Order(restaurant, orderId);

        // Create some menu items
        item1 = new MenuItem("Burger", 20,5.99);
        item2 = new MenuItem("Fries", 50,2.99);
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
    void testGetTotalPrice() {
        // Add items to the order
        order.addItem(item1);
        order.addItem(item2);

        // Mock the calculatePrice method of the restaurant
        when(restaurant.calculatePrice(order)).thenReturn(8.98);

        // Test the getTotalPrice method
        assertEquals(8.98, order.getTotalPrice(), 0.001);

        // Verify that the restaurant's method was called
        verify(restaurant, times(1)).calculatePrice(order);
    }

    @Test
    void testOrderIdIsGenerated() {
        assertEquals(orderId, order.getOrderId());
    }
}
