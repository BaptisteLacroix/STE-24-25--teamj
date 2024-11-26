package java.fr.unice.polytech.equipe.j.payment;

import java.fr.unice.polytech.equipe.j.order.IndividualOrder;
import java.fr.unice.polytech.equipe.j.order.OrderManager;
import java.fr.unice.polytech.equipe.j.order.OrderStatus;
import fr.unice.polytech.equipe.j.payment.strategy.PaymentMethod;
import fr.unice.polytech.equipe.j.restaurant.backend.IRestaurant;
import fr.unice.polytech.equipe.j.restaurant.backend.Restaurant;
import fr.unice.polytech.equipe.j.restaurant.backend.RestaurantProxy;
import java.fr.unice.polytech.equipe.j.user.CampusUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests for the PaymentClient class.
 */
class PaymentClientTest {

    private IRestaurant restaurantProxyMock;
    private IRestaurant restaurantMock;
    private OrderManager orderManager;
    private CampusUser campusUserMock;
    private IndividualOrder order;

    @BeforeEach
    void setUp() {
        restaurantProxyMock = mock(RestaurantProxy.class);
        restaurantMock = mock(Restaurant.class);

        // Initialize the actual OrderManager instead of mocking it
        orderManager = new OrderManager(restaurantProxyMock);

        campusUserMock = mock(CampusUser.class);
        order = mock(IndividualOrder.class);
        when(order.getStatus()).thenReturn(OrderStatus.PENDING);
        when(order.getUser()).thenReturn(campusUserMock);
        when(order.getRestaurant()).thenReturn(restaurantProxyMock);
        when(restaurantMock.isOrderValid(any())).thenReturn(true);
        when(restaurantProxyMock.isOrderValid(any())).thenReturn(true);
    }

    @Test
    void testMakePaymentSuccess() {
        when(campusUserMock.getDefaultPaymentMethod()).thenReturn(PaymentMethod.CREDIT_CARD);
        when(restaurantProxyMock.getTotalPrice(any())).thenReturn(100.0);

        // Call the real validateOrder method
        Transaction tr = orderManager.validateOrder(order);

        assertEquals(100.0, tr.getAmount(), 0.001);
        assertEquals("CREDIT_CARD", tr.getPaymentMethod());
    }

    @Test
    void testMakePaymentFailure() {
        when(campusUserMock.getDefaultPaymentMethod()).thenReturn(PaymentMethod.CREDIT_CARD);
        when(restaurantProxyMock.getTotalPrice(any())).thenReturn(600.0);

        // Call the real validateOrder method
        assertThrows(IllegalArgumentException.class, () -> orderManager.validateOrder(order));
    }

    @Test
    void testMultiplePayments() {
        ArrayList<Transaction> transactions = new ArrayList<>();

        when(campusUserMock.getDefaultPaymentMethod()).thenReturn(PaymentMethod.CREDIT_CARD);
        when(restaurantProxyMock.getTotalPrice(any())).thenReturn(60.0);
        transactions.add(orderManager.validateOrder(order));

        when(campusUserMock.getDefaultPaymentMethod()).thenReturn(PaymentMethod.PAYPAL);
        when(restaurantProxyMock.getTotalPrice(any())).thenReturn(250.0);
        transactions.add(orderManager.validateOrder(order));

        when(campusUserMock.getDefaultPaymentMethod()).thenReturn(PaymentMethod.PAYLIB);
        when(restaurantProxyMock.getTotalPrice(any())).thenReturn(22.0);
        transactions.add(orderManager.validateOrder(order));

        assertEquals(3, transactions.size(), "There should be three successful transactions recorded");
    }

    @Test
    void testTransactionDetails() {
        when(campusUserMock.getDefaultPaymentMethod()).thenReturn(PaymentMethod.PAYPAL);
        when(restaurantProxyMock.getTotalPrice(any())).thenReturn(60.0);

        // Call the real validateOrder method
        Transaction tr = orderManager.validateOrder(order);

        assertEquals(60.0, tr.getAmount(), 0.001);
        assertEquals("PAYPAL", tr.getPaymentMethod());
        assertNotNull(tr.getTimestamp(), "Transaction timestamp should not be null");
    }
}
