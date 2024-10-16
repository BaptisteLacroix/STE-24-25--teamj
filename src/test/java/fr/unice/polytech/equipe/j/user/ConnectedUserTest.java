package fr.unice.polytech.equipe.j.user;

import fr.unice.polytech.equipe.j.order.DeliveryDetails;
import fr.unice.polytech.equipe.j.order.GroupOrder;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.payment.Transaction;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.order.OrderManager;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ConnectedUserTest {

    private ConnectedUser connectedUser;
    private OrderManager mockOrderManager;
    private Restaurant mockRestaurant;
    private MenuItem mockMenuItem;
    private Order mockOrder;
    private DeliveryDetails mockDeliveryDetails;

    @BeforeEach
    void setUp() {
        // Mocking dependencies
        mockOrderManager = mock(OrderManager.class);
        mockRestaurant = mock(Restaurant.class);
        mockMenuItem = mock(MenuItem.class);
        mockOrder = mock(Order.class);
        mockDeliveryDetails = mock(DeliveryDetails.class);

        // Mocking Restaurant behavior
        when(mockRestaurant.capacityCheck()).thenReturn(true);
        when(mockRestaurant.isOrderValid(any(Order.class))).thenReturn(true);

        // Create ConnectedUser with the mock OrderManager
        connectedUser = new ConnectedUser("user@example.com", "password123", 100.0, mockOrderManager);

        // Mock OrderManager behaviors
        when(mockOrderManager.startSingleOrder(mockRestaurant, mockDeliveryDetails)).thenReturn(mockOrder);
        when(mockOrderManager.startGroupOrder(mockDeliveryDetails)).thenReturn(mock(GroupOrder.class));
    }

    @Test
    void testStartIndividualOrder() {
        // Act
        connectedUser.startIndividualOrder(mockRestaurant, mockDeliveryDetails);

        // Assert
        assertNotNull(connectedUser.getCurrentOrder(), "Order should be initialized");
    }

    @Test
    void testStartGroupOrder() {
        // Act
        connectedUser.startGroupOrder(mockDeliveryDetails);

        // Assert
        assertNotNull(connectedUser.getCurrentGroupOrder(), "Group order should be initialized");
    }

    @Test
    void testAddItemToOrder() {
        // Arrange
        connectedUser.startIndividualOrder(mockRestaurant, mockDeliveryDetails);

        // Act
        connectedUser.addItemToOrder(mockRestaurant, mockMenuItem);

        // Assert
        verify(mockOrderManager, times(1)).addItemToOrder(mockOrder, mockRestaurant, mockMenuItem);
    }

    @Test
    void testValidateIndividualOrder() {
        // Arrange
        connectedUser.startIndividualOrder(mockRestaurant, mockDeliveryDetails);

        // Act
        connectedUser.validateIndividualOrder(mockRestaurant);

        // Assert
        verify(mockOrderManager, times(1)).validateIndividualOrder(any(Transaction.class), eq(mockOrder), eq(mockRestaurant));
    }

    @Test
    void testOrderPaid() {
        // Arrange
        connectedUser.startIndividualOrder(mockRestaurant, mockDeliveryDetails);
        Order mockPaidOrder = mock(Order.class);

        // Act
        connectedUser.orderPaid(mockPaidOrder);

        // Assert
        assertEquals(1, connectedUser.getOrdersHistory().size(), "Order history should have one entry after payment");
        assertTrue(connectedUser.getOrdersHistory().contains(mockPaidOrder), "Order history should contain the paid order");
    }

    @Test
    void testToString() {
        // Arrange
        connectedUser.startIndividualOrder(mockRestaurant, mockDeliveryDetails);

        // Act
        connectedUser.orderPaid(mockOrder);
        String result = connectedUser.toString();

        // Assert
        assertTrue(result.contains("1 orders"), "ToString should reflect the number of orders in the history");
    }
}

