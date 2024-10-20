package fr.unice.polytech.equipe.j.user;

import fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.order.IndividualOrder;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderManager;
import fr.unice.polytech.equipe.j.payment.Transaction;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class CampusUserTest {

    private CampusUser campusUser;
    private OrderManager mockOrderManager;
    private Restaurant mockRestaurant;
    private MenuItem mockMenuItem;
    private Order order;
    private IndividualOrder mockIndividualOrder;
    private DeliveryDetails mockDeliveryDetails;

    @BeforeEach
    void setUp() {
        // setup time
        Clock clock = Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris"));
        TimeUtils.setClock(clock);

        // Create CampusUser with the mock OrderManager
        campusUser = new CampusUser("user@example.com", "password123", mockOrderManager);
        order = spy(new Order(mockRestaurant, campusUser));

        // Mocking dependencies
        mockRestaurant = mock(Restaurant.class);
        mockMenuItem = mock(MenuItem.class);
        mockIndividualOrder = mock(IndividualOrder.class);
        mockDeliveryDetails = mock(DeliveryDetails.class);

        // Mocking Restaurant behavior
        when(mockRestaurant.capacityCheck()).thenReturn(true);
        when(mockRestaurant.isOrderValid(any(Order.class))).thenReturn(true);
    }

    @Test
    void testAddItemToOrder() {
        order.addItem(mockMenuItem);
        verify(order, times(1)).addItem(eq(mockMenuItem));
    }

    @Test
    void testValidateIndividualOrder() {
        // Arrange
        IndividualOrder individualOrder = mock(IndividualOrder.class);
        campusUser.setCurrentOrder(individualOrder);
        // Act
        campusUser.validateOrder();
        // Assert
        verify(mockOrderManager, times(1)).validateOrder(any(Transaction.class), eq(individualOrder));
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

