package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderBuilder;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.restaurant.RestaurantFacade;
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
        item1 = new MenuItem("Pizza", 12.99);
        item2 = new MenuItem("Salad", 6.99);
    }

    @Test
    void testBuildOrderWithItems() {
        LocalDateTime deliveryTime = LocalDateTime.now().plusHours(2);
        Order order = orderBuilder
                .setRestaurant(new RestaurantFacade(restaurant))
                .addMenuItem(item1)
                .addMenuItem(item2)
                .setDeliveryDetails(new DeliveryDetails(DeliveryLocationManager.getInstance().getPredefinedLocations().getFirst(), deliveryTime))
                .build();

        // Validate the order properties
        assertEquals(restaurant, order.getRestaurantFacade());
        assertTrue(order.getItems().containsAll(Arrays.asList(item1, item2)));
        assertEquals(2, order.getItems().size());
        assertEquals(deliveryTime, order.getDeliveryTime());
    }

    @Test
    void testBuildOrderWithoutItems() {
        // Set up the order without items
        Order order = orderBuilder.setRestaurant(new RestaurantFacade(restaurant)).build();

        // Validate that the order has no items
        assertEquals(0, order.getItems().size());
    }
}
