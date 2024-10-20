package fr.unice.polytech.equipe.j.payment;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the PaymentProcessor implementations.
 */
public class PaymentProcessorTest {

    @Test
    public void testCreditCardProcessorSuccess() {
        PaymentProcessor processor = new CreditCardProcessor();
        boolean result = processor.processPayment(100.0);
        assertTrue(result, "Credit card payment should succeed for amount <= 500");
    }

    @Test
    public void testCreditCardProcessorFailure() {
        PaymentProcessor processor = new CreditCardProcessor();
        boolean result = processor.processPayment(600.0);
        assertFalse(result, "Credit card payment should fail for amount > 500");
    }

    @Test
    public void testPayPalProcessorSuccess() {
        PaymentProcessor processor = new PayPalProcessor();
        boolean result = processor.processPayment(500.0);
        assertTrue(result, "PayPal payment should succeed for amount <= 1000");
    }

    @Test
    public void testPayPalProcessorFailure() {
        PaymentProcessor processor = new PayPalProcessor();
        boolean result = processor.processPayment(1500.0);
        assertFalse(result, "PayPal payment should fail for amount > 1000");
    }

    @Test
    public void testPaylibProcessorSuccess() {
        PaymentProcessor processor = new PaylibProcessor();
        boolean result = processor.processPayment(1000.0);
        assertTrue(result, "Paylib payment should succeed for amount <= 2000");
    }

    @Test
    public void testPaylibProcessorFailure() {
        PaymentProcessor processor = new PaylibProcessor();
        boolean result = processor.processPayment(2500.0);
        assertFalse(result, "Paylib payment should fail for amount > 2000");
    }
}
