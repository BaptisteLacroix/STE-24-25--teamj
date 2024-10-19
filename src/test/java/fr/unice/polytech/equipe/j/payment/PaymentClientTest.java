package fr.unice.polytech.equipe.j.payment;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

/**
 * Tests for the PaymentClient class.
 */
public class PaymentClientTest {

    @Test
    public void testMakePaymentSuccess() {
        PaymentClient client = new PaymentClient();
        client.makePayment(100.0, PaymentMethod.CREDIT_CARD);

        List<Transaction> transactions = client.getTransactions();
        assertEquals(1, transactions.size(), "There should be one successful transaction recorded");

        Transaction transaction = transactions.get(0);
        assertEquals(100.0, transaction.getAmount(), 0.001);
        assertEquals("CREDIT_CARD", transaction.getPaymentMethod());
    }

    @Test
    public void testMakePaymentFailure() {
        PaymentClient client = new PaymentClient();
        client.makePayment(600.0, PaymentMethod.CREDIT_CARD);

        List<Transaction> transactions = client.getTransactions();
        assertEquals(0, transactions.size(), "There should be no transactions recorded for failed payments");
    }

    @Test
    public void testMultiplePayments() {
        PaymentClient client = new PaymentClient();
        client.makePayment(100.0, PaymentMethod.CREDIT_CARD);
        client.makePayment(250.0, PaymentMethod.PAYPAL);
        client.makePayment(300.0, PaymentMethod.PAYLIB);

        List<Transaction> transactions = client.getTransactions();
        assertEquals(3, transactions.size(), "There should be three successful transactions recorded");
    }

    @Test
    public void testTransactionDetails() {
        PaymentClient client = new PaymentClient();
        client.makePayment(100.0, PaymentMethod.PAYPAL);

        Transaction transaction = client.getTransactions().get(0);
        assertEquals(100.0, transaction.getAmount(), 0.001);
        assertEquals("PAYPAL", transaction.getPaymentMethod());
        assertNotNull(transaction.getTimestamp(), "Transaction timestamp should not be null");
    }
}
