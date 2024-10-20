package fr.unice.polytech.equipe.j.user;

import fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
import fr.unice.polytech.equipe.j.order.IndividualOrder;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderManager;
import fr.unice.polytech.equipe.j.order.OrderStatus;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.restaurant.RestaurantServiceManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CampusUserTest {

    private CampusUser campusUser;
    private Restaurant restaurant;
    private MenuItem mockMenuItem;
    private Order order;
    private DeliveryDetails deliveryDetails;

    @BeforeEach
    void setUp() {
        // setup time
        TimeUtils.setClock(Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris")));

        DeliveryLocation deliveryLocation = DeliveryLocationManager.getInstance().getPredefinedLocations().getFirst();
        this.deliveryDetails = new DeliveryDetails(deliveryLocation, TimeUtils.getNow().plusHours(7).plusMinutes(29));

        // Create CampusUser with the mock OrderManager
        campusUser = new CampusUser("user@example.com", "password123", new OrderManager());
        order = spy(new Order(restaurant, campusUser));

        // Mocking dependencies
        restaurant = RestaurantServiceManager.getInstance().getRestaurants().getFirst();
        mockMenuItem = mock(MenuItem.class);
        // Mocking Restaurant behavior
//        when(restaurant.capacityCheck()).thenReturn(true);
//        when(restaurant.isOrderValid(any(Order.class))).thenReturn(true);
    }

    @Test
    void testAddItemToOrder() {
        order.addItem(mockMenuItem);
        verify(order, times(1)).addItem(eq(mockMenuItem));
    }

    @Test
    void testValidateIndividualOrder() {
        // Arrange
        IndividualOrder individualOrder = new IndividualOrder(restaurant, deliveryDetails, campusUser);

        campusUser.setCurrentOrder(individualOrder);
        // Act
        campusUser.validateOrder();
        // Assert
        assertEquals(OrderStatus.VALIDATED, order.getStatus());
    }

    @Test
    void testOnOrderPaid() {
        // Arrange
        IndividualOrder individualOrder = mock(IndividualOrder.class);
        Order mockPaidOrder = mock(Order.class);

        // Act
        campusUser.onOrderPaid(mockPaidOrder);

        // Assert
        assertEquals(1, campusUser.getOrdersHistory().size(), "Order history should have one entry after payment");
        assertTrue(campusUser.getOrdersHistory().contains(mockPaidOrder), "Order history should contain the paid order");
    }

    @Test
    void testToString() {
        // Act
        campusUser.onOrderPaid(order);
        String result = campusUser.toString();

        // Assert
        assertTrue(result.contains("1 orders"), "ToString should reflect the number of orders in the history");
    }
}

