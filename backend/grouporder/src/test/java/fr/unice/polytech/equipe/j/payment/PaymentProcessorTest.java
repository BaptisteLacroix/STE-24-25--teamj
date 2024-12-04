package java.fr.unice.polytech.equipe.j.payment;

import fr.unice.polytech.equipe.j.payment.strategy.CreditCardProcessor;
import fr.unice.polytech.equipe.j.payment.strategy.PayPalProcessor;
import fr.unice.polytech.equipe.j.payment.strategy.PaylibProcessor;
import fr.unice.polytech.equipe.j.payment.strategy.PaymentProcessor;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the PaymentProcessor implementations.
 */
class PaymentProcessorTest {

    @Test
    void testCreditCardProcessorSuccess() {
        PaymentProcessor processor = new CreditCardProcessor();
        assertDoesNotThrow(() -> processor.processPayment(100.0));
    }

    @Test
    void testCreditCardProcessorFailure() {
        PaymentProcessor processor = new CreditCardProcessor();
        assertThrows(IllegalArgumentException.class, () -> processor.processPayment(600.0));
    }

    @Test
    void testPayPalProcessorSuccess() {
        PaymentProcessor processor = new PayPalProcessor();
        assertDoesNotThrow(() -> processor.processPayment(500.0));
    }

    @Test
    void testPayPalProcessorFailure() {
        PaymentProcessor processor = new PayPalProcessor();
        assertThrows(IllegalArgumentException.class, () -> processor.processPayment(1500.0));
    }

    @Test
    void testPaylibProcessorSuccess() {
        PaymentProcessor processor = new PaylibProcessor();
        assertDoesNotThrow(() -> processor.processPayment(1000.0));
    }

    @Test
    void testPaylibProcessorFailure() {
        PaymentProcessor processor = new PaylibProcessor();
        assertThrows(IllegalArgumentException.class, () -> processor.processPayment(2500.0));
    }
}
