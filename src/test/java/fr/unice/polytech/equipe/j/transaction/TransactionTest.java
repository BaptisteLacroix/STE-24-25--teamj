package fr.unice.polytech.equipe.j.transaction;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.payment.CheckoutObserver;
import fr.unice.polytech.equipe.j.payment.Transaction;
import fr.unice.polytech.equipe.j.user.CampusUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class TransactionTest {

    private CampusUser user;
    private Order order;
    private Transaction transaction;
    private CheckoutObserver observer1;
    private CheckoutObserver observer2;

    @BeforeEach
    void setUp() {
        user = mock(CampusUser.class);
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
        transaction.addObserver(observer1);
        transaction.addObserver(observer2);
        transaction.proceedCheckout(order, 50.0);
        verify(observer1, times(1)).onOrderPaid(order);
        verify(observer2, times(1)).onOrderPaid(order);
    }

    @Test
    void testProceedCheckout_ObserversNotified() {
        transaction.addObserver(observer1);
        transaction.addObserver(observer2);
        transaction.proceedCheckout(order, 50.0);
        verify(observer1, times(1)).onOrderPaid(order);
        verify(observer2, times(1)).onOrderPaid(order);
    }
}
