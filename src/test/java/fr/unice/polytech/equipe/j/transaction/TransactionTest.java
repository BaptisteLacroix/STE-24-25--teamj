package fr.unice.polytech.equipe.j.transaction;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.payment.CheckoutObserver;
import fr.unice.polytech.equipe.j.payment.Transaction;
import fr.unice.polytech.equipe.j.user.ConnectedUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyDouble;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class TransactionTest {

    private ConnectedUser user;
    private Order order;
    private Transaction transaction;
    private CheckoutObserver observer1;
    private CheckoutObserver observer2;

    @BeforeEach
    void setUp() {
        user = mock(ConnectedUser.class);
        order = mock(Order.class);
        observer1 = mock(CheckoutObserver.class);
        observer2 = mock(CheckoutObserver.class);
        transaction = new Transaction(user);
    }

    @Test
    void testAddObserver() {
        transaction.addObserver(observer1);
        transaction.addObserver(observer2);
        // Simulate a successful transaction
        when(order.getTotalPrice()).thenReturn(10.0);
        when(user.getAccountBalance()).thenReturn(50.0);

        // Perform checkout
        transaction.proceedCheckout(order.getOrderUUID());

        // Verify both observers are notified
        verify(observer1, times(1)).notifyCheckoutSuccess(order);
        verify(observer2, times(1)).notifyCheckoutSuccess(order);
    }

    @Test
    void testProceedCheckout_Success() {
        // Set up mock behavior
        when(order.getTotalPrice()).thenReturn(20.0);
        when(user.getAccountBalance()).thenReturn(100.0);

        // Perform the checkout
        transaction.proceedCheckout(order.getOrderUUID());

        // Verify account balance is updated
        verify(user, times(1)).setAccountBalance(80.0); // 100.0 - 20.0
    }

    @Test
    void testProceedCheckout_InsufficientFunds() {
        // Set up mock behavior for insufficient funds
        when(order.getTotalPrice()).thenReturn(200.0);
        when(user.getAccountBalance()).thenReturn(100.0);

        // Ensure an exception is thrown
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            transaction.proceedCheckout(order.getOrderUUID());
        });

        assertEquals("Insufficient funds", exception.getMessage());

        // Verify that the user's balance is not changed
        verify(user, never()).setAccountBalance(anyDouble());
        verify(observer1, never()).notifyCheckoutSuccess(any());
    }

    @Test
    void testProceedCheckout_ObserversNotified() {
        // Add observers
        transaction.addObserver(observer1);
        transaction.addObserver(observer2);

        // Set up mock behavior for a successful transaction
        when(order.getTotalPrice()).thenReturn(50.0);
        when(user.getAccountBalance()).thenReturn(100.0);

        // Perform the checkout
        transaction.proceedCheckout(order.getOrderUUID());

        // Verify both observers are notified
        verify(observer1, times(1)).notifyCheckoutSuccess(order);
        verify(observer2, times(1)).notifyCheckoutSuccess(order);
    }

    @Test
    void testProceedCheckout_AccountBalanceNotUpdatedOnFailure() {
        // Set up mock behavior for insufficient funds
        when(order.getTotalPrice()).thenReturn(120.0);
        when(user.getAccountBalance()).thenReturn(100.0);

        // Ensure an exception is thrown
        assertThrows(IllegalArgumentException.class, () -> transaction.proceedCheckout(order.getOrderUUID()));

        // Verify that the user's balance was not updated
        verify(user, never()).setAccountBalance(anyDouble());

        // Verify that observers were not notified
        verify(observer1, never()).notifyCheckoutSuccess(any());
        verify(observer2, never()).notifyCheckoutSuccess(any());
    }
}
