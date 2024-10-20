package fr.unice.polytech.equipe.j.user;

import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.order.GroupOrder;
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
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CampusUserTest {

    private CampusUser campusUser;
    private OrderManager mockOrderManager;
    private Restaurant mockRestaurant;
    private MenuItem mockMenuItem;
    private Order mockOrder;
    private IndividualOrder mockIndividualOrder;
    private DeliveryDetails mockDeliveryDetails;
    private Clock clock;

    @BeforeEach
    void setUp() {
        clock = Clock.fixed(Instant.parse("2024-10-18T12:00:00Z"), ZoneId.of("Europe/Paris"));
        // Mocking dependencies
        mockOrderManager = mock(OrderManager.class);
        mockRestaurant = mock(Restaurant.class);
        mockMenuItem = mock(MenuItem.class);
        mockOrder = mock(Order.class);
        mockIndividualOrder = mock(IndividualOrder.class);
        mockDeliveryDetails = mock(DeliveryDetails.class);

        // Mocking Restaurant behavior
        when(mockRestaurant.capacityCheck()).thenReturn(true);
        when(mockRestaurant.isOrderValid(any(Order.class))).thenReturn(true);

        // Create ConnectedUser with the mock OrderManager
        campusUser = new CampusUser("user@example.com", "password123", mockOrderManager);

        // Mock OrderManager behaviors
        when(mockOrderManager.startSingleOrder(mockRestaurant, mockDeliveryDetails)).thenReturn(mockIndividualOrder);
        when(mockOrderManager.startGroupOrder(mockDeliveryDetails)).thenReturn(mock(GroupOrder.class));
    }

    @Test
    void testStartIndividualOrder() {
        // Act
        campusUser.startIndividualOrder(mockRestaurant, mockDeliveryDetails);

        // Assert
        assertNotNull(campusUser.getCurrentOrder(), "Order should be initialized");
    }

    @Test
    void testStartGroupOrder() {
        // Act
        campusUser.createGroupOrder(mockDeliveryDetails);

        // Assert
        assertNotNull(campusUser.getCurrentGroupOrder(), "Group order should be initialized");
    }

    @Test
    void testAddItemToOrder() {
        // Arrange
        campusUser.startIndividualOrder(mockRestaurant, mockDeliveryDetails);
        when(mockDeliveryDetails.getDeliveryTime()).thenReturn(java.util.Optional.of(java.time.LocalDateTime.now(clock).plusHours(1)));

        // Act
        campusUser.addItemToOrder(mockRestaurant, mockMenuItem);

        // Assert
        verify(mockOrderManager, times(1)).addItemToOrder(any(IndividualOrder.class), eq(mockRestaurant), eq(mockMenuItem));
    }

    @Test
    void testValidateIndividualOrder() {
        // Arrange
        IndividualOrder individualOrder = mock(IndividualOrder.class);
        when(mockOrderManager.startSingleOrder(mockRestaurant, mockDeliveryDetails)).thenReturn(individualOrder);

        campusUser.startIndividualOrder(mockRestaurant, mockDeliveryDetails);

        // Act
        campusUser.validateOrder();

        // Assert
    }

    @Test
    void testOrderPaid() {
        // Arrange
        campusUser.startIndividualOrder(mockRestaurant, mockDeliveryDetails);
        Order mockPaidOrder = mock(Order.class);

        // Act
        campusUser.orderPaid(mockPaidOrder);

        // Assert
        assertEquals(1, campusUser.getOrdersHistory().size(), "Order history should have one entry after payment");
        assertTrue(campusUser.getOrdersHistory().contains(mockPaidOrder), "Order history should contain the paid order");
    }

    @Test
    void testToString() {
        // Arrange
        campusUser.startIndividualOrder(mockRestaurant, mockDeliveryDetails);

        // Act
        campusUser.orderPaid(mockOrder);
        String result = campusUser.toString();

        // Assert
        assertTrue(result.contains("1 orders"), "ToString should reflect the number of orders in the history");
    }
}

