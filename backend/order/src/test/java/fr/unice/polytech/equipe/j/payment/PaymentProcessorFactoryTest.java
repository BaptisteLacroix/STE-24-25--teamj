package fr.unice.polytech.equipe.j.payment;

import fr.unice.polytech.equipe.j.payment.strategy.CreditCardProcessor;
import fr.unice.polytech.equipe.j.payment.strategy.PayPalProcessor;
import fr.unice.polytech.equipe.j.payment.strategy.PaylibProcessor;
import fr.unice.polytech.equipe.j.payment.strategy.PaymentMethod;
import fr.unice.polytech.equipe.j.payment.strategy.PaymentProcessor;
import fr.unice.polytech.equipe.j.payment.strategy.PaymentProcessorFactory;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the PaymentProcessorFactory class.
 */
public class PaymentProcessorFactoryTest {

    @Test
    public void testCreateCreditCardProcessor() {
        PaymentProcessor processor = PaymentProcessorFactory.createPaymentProcessor(PaymentMethod.CREDIT_CARD);
        assertTrue(processor instanceof CreditCardProcessor, "Should create an instance of CreditCardProcessor");
    }

    @Test
    public void testCreatePayPalProcessor() {
        PaymentProcessor processor = PaymentProcessorFactory.createPaymentProcessor(PaymentMethod.PAYPAL);
        assertTrue(processor instanceof PayPalProcessor, "Should create an instance of PayPalProcessor");
    }

    @Test
    public void testCreatePaylibProcessor() {
        PaymentProcessor processor = PaymentProcessorFactory.createPaymentProcessor(PaymentMethod.PAYLIB);
        assertTrue(processor instanceof PaylibProcessor, "Should create an instance of PaylibProcessor");
    }

    @Test
    public void testInvalidPaymentMethod() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            PaymentProcessorFactory.createPaymentProcessor(null);
        });
//        assertEquals("Invalid payment method: null", exception.getMessage());
    }
}
