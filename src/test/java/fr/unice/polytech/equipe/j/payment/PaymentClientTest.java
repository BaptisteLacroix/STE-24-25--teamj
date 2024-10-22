package fr.unice.polytech.equipe.j.payment;

import fr.unice.polytech.equipe.j.order.Order;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import fr.unice.polytech.equipe.j.order.OrderManager;

/**
 * Tests for the PaymentClient class.
 */
public class PaymentClientTest {

    @Test
    public void testMakePaymentSuccess() {
        Clock clock = new Clock() {
            @Override
            public ZoneId getZone() {
                return null;
            }

            @Override
            public Clock withZone(ZoneId zone) {
                return null;
            }

            @Override
            public Instant instant() {
                return null;
            }
        };
        OrderManager orderManager = new OrderManager();
        Transaction tr = orderManager.makePayment(100.0, PaymentMethod.CREDIT_CARD,null);

        assertEquals(100.0, tr.getAmount(), 0.001);
        assertEquals("CREDIT_CARD", tr.getPaymentMethod());
    }

    @Test
    public void testMakePaymentFailure() {
        Clock clock = new Clock() {
            @Override
            public ZoneId getZone() {
                return null;
            }

            @Override
            public Clock withZone(ZoneId zone) {
                return null;
            }

            @Override
            public Instant instant() {
                return null;
            }
        };
        OrderManager orderManager = new OrderManager();
        Transaction tr = orderManager.makePayment(600.0, PaymentMethod.CREDIT_CARD,null);


        assertEquals(null, tr, "There should be no transactions recorded for failed payments");
    }

    @Test
    public void testMultiplePayments() {

        Clock clock = new Clock() {
            @Override
            public ZoneId getZone() {
                return null;
            }

            @Override
            public Clock withZone(ZoneId zone) {
                return null;
            }

            @Override
            public Instant instant() {
                return null;
            }
        };
        OrderManager orderManager = new OrderManager();
        ArrayList<Transaction> transactions = new ArrayList<>();

        transactions.add(orderManager.makePayment(60.0, PaymentMethod.CREDIT_CARD,null));
        transactions.add(orderManager.makePayment(250.0, PaymentMethod.PAYPAL,null));
        transactions.add(orderManager.makePayment(22.0, PaymentMethod.PAYLIB,null));


        assertEquals(3, transactions.size(), "There should be three successful transactions recorded");
    }

    @Test
    public void testTransactionDetails() {
        Clock clock = new Clock() {
            @Override
            public ZoneId getZone() {
                return null;
            }

            @Override
            public Clock withZone(ZoneId zone) {
                return null;
            }

            @Override
            public Instant instant() {
                return null;
            }
        };
        OrderManager orderManager = new OrderManager();
        Transaction tr = orderManager.makePayment(60.0, PaymentMethod.PAYPAL,null);

        assertEquals(60.0, tr.getAmount(), 0.001);
        assertEquals("PAYPAL", tr.getPaymentMethod());
        assertNotNull(tr.getTimestamp(), "Transaction timestamp should not be null");
    }
}
