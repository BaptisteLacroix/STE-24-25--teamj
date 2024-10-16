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
        assertEquals(2, transaction.getObservers().size());
    }

    @Test
    void testRemoveObserver() {
        transaction.addObserver(observer1);
        transaction.addObserver(observer2);
        transaction.removeObserver(observer1);
        assertEquals(1, transaction.getObservers().size());
    }

    @Test
    void testProceedCheckout_Success() {
        when(user.getAccountBalance()).thenReturn(100.0);
        transaction.addObserver(observer1);
        transaction.addObserver(observer2);
        transaction.proceedCheckout(order, 50.0);
        verify(user, times(1)).setAccountBalance(50.0);
        verify(observer1, times(1)).orderPaid(order);
        verify(observer2, times(1)).orderPaid(order);
    }

    @Test
    void testProceedCheckout_InsufficientFunds() {
        when(user.getAccountBalance()).thenReturn(100.0);
        assertThrows(IllegalArgumentException.class, () -> transaction.proceedCheckout(order, 150.0));
        verify(user, never()).setAccountBalance(anyDouble());
        verify(observer1, never()).orderPaid(any(Order.class));
        verify(observer2, never()).orderPaid(any(Order.class));
    }

    @Test
    void testProceedCheckout_ObserversNotified() {
        when(user.getAccountBalance()).thenReturn(100.0);
        transaction.addObserver(observer1);
        transaction.addObserver(observer2);
        transaction.proceedCheckout(order, 50.0);
        verify(observer1, times(1)).orderPaid(order);
        verify(observer2, times(1)).orderPaid(order);
    }

    @Test
    void testProceedCheckout_AccountBalanceNotUpdatedOnFailure() {
        when(user.getAccountBalance()).thenReturn(100.0);
        assertThrows(IllegalArgumentException.class, () -> transaction.proceedCheckout(order, 150.0));
        verify(user, never()).setAccountBalance(anyDouble());
    }
}
