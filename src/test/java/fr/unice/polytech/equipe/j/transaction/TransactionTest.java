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
        throw new RuntimeException("not yet implemented");
    }

    @Test
    void testProceedCheckout_Success() {
        throw new RuntimeException("not yet implemented");
    }

    @Test
    void testProceedCheckout_InsufficientFunds() {
        throw new RuntimeException("not yet implemented");
    }

    @Test
    void testProceedCheckout_ObserversNotified() {
        throw new RuntimeException("not yet implemented");
    }

    @Test
    void testProceedCheckout_AccountBalanceNotUpdatedOnFailure() {
        throw new RuntimeException("not yet implemented");
    }
}
