package fr.unice.polytech.equipe.j.tests.order;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderBuilder;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class OrderBuilderTest {

    private OrderBuilder orderBuilder;
    private Restaurant restaurant;
    private MenuItem item1;
    private MenuItem item2;

    @BeforeEach
    void setUp() {
        restaurant = mock(Restaurant.class);
        orderBuilder = new OrderBuilder();
        item1 = new MenuItem("Pizza", 6,12.99,1);
        item2 = new MenuItem("Salad",5, 6.99,1);
    }

    @Test
    void testBuildOrderWithItems() {
        Order order = orderBuilder
                .setRestaurant(restaurant)
                .addMenuItem(item1)
                .addMenuItem(item2)
                .build();

        // Validate the order properties
        assertEquals(restaurant, order.getRestaurant());
        assertTrue(order.getItems().containsAll(Arrays.asList(item1, item2)));
        assertEquals(2, order.getItems().size());
    }

    @Test
    void testBuildOrderWithoutItems() {
        // Set up the order without items
        Order order = orderBuilder.setRestaurant(restaurant).build();

        // Validate that the order has no items
        assertEquals(0, order.getItems().size());
    }
}
