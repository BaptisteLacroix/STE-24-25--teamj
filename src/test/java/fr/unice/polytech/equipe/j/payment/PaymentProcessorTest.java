package fr.unice.polytech.equipe.j.payment;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit test class for PaymentProcessor.
 */
public class PaymentProcessorTest {

    private PaymentProcessor paymentProcessor;

    /**
     * Sets up a new PaymentProcessor instance before each test.
     */
    @BeforeEach
    public void setUp() {
        paymentProcessor = new PaymentProcessor(100.0, "Credit Card", "EUR", "CUST12345", "4111111111111111");
    }

    /**
     * Test to verify if payment is processed successfully with valid details.
     */
    @Test
    public void testProcessPayment_Success() {
        boolean result = paymentProcessor.processPayment();
        assertTrue(result, "The payment should be processed successfully.");
        assertEquals("PROCESSED", paymentProcessor.getPaymentStatus(), "Payment status should be PROCESSED.");
    }

    /**
     * Test to verify if payment processing fails when Visa card number is invalid.
     */
    @Test
    public void testProcessPayment_Failure_InvalidVisaCard() {
        PaymentProcessor invalidCardProcessor = new PaymentProcessor(100.0, "Credit Card", "USD", "CUST12345", "1234567890123456");
        boolean result = invalidCardProcessor.processPayment();
        assertFalse(result, "The payment should not be processed due to an invalid Visa card number.");
        assertEquals("FAILED", invalidCardProcessor.getPaymentStatus(), "Payment status should be FAILED.");
    }

    /**
     * Test to verify if payment processing fails when validation fails.
     */
    @Test
    public void testProcessPayment_Failure_InvalidPaymentMethod() {
        paymentProcessor.setPaymentMethod(""); // Invalid payment method
        boolean result = paymentProcessor.processPayment();
        assertFalse(result, "The payment should not be processed due to invalid payment method.");
        assertEquals("FAILED", paymentProcessor.getPaymentStatus(), "Payment status should be FAILED.");
    }

    /**
     * Test to verify if a payment can be canceled successfully.
     */
    @Test
    public void testCancelPayment_Success() {
        boolean result = paymentProcessor.cancelPayment();
        assertTrue(result, "The payment should be canceled successfully.");
        assertEquals("CANCELED", paymentProcessor.getPaymentStatus(), "Payment status should be CANCELED.");
    }

    /**
     * Test to verify if an already processed payment cannot be canceled.
     */
    @Test
    public void testCancelPayment_AfterProcessing() {
        paymentProcessor.processPayment();
        boolean result = paymentProcessor.cancelPayment();
        assertFalse(result, "The payment should not be canceled after it has been processed.");
        assertEquals("PROCESSED", paymentProcessor.getPaymentStatus(), "Payment status should remain PROCESSED.");
    }

    /**
     * Test to verify if a processed payment can be refunded.
     */
    @Test
    public void testRefundPayment_Success() {
        paymentProcessor.processPayment();
        boolean result = paymentProcessor.refundPayment();
        assertTrue(result, "The payment should be refunded successfully.");
        assertEquals("REFUNDED", paymentProcessor.getPaymentStatus(), "Payment status should be REFUNDED.");
    }

    /**
     * Test to verify if a pending payment cannot be refunded.
     */
    @Test
    public void testRefundPayment_Failure() {
        boolean result = paymentProcessor.refundPayment();
        assertFalse(result, "The payment should not be refunded as it has not been processed yet.");
        assertEquals("PENDING", paymentProcessor.getPaymentStatus(), "Payment status should remain PENDING.");
    }

    /**
     * Test to verify the generation of a unique transaction ID.
     */
    @Test
    public void testGenerateTransactionId() {
        String transactionId = paymentProcessor.getTransactionId();
        assertNotNull(transactionId, "Transaction ID should not be null.");
        assertTrue(transactionId.startsWith("TXN"), "Transaction ID should start with 'TXN'.");
    }

    /**
     * Test to verify if an invalid Visa card number is detected correctly.
     */
    @Test
    public void testValidateVisaCardNumber_Failure() {
        PaymentProcessor invalidCardProcessor = new PaymentProcessor(100.0, "Credit Card", "USD", "CUST12345", "1234567890123456");
        boolean result = invalidCardProcessor.validateVisaCardNumber();
        assertFalse(result, "The Visa card number should be considered invalid.");
    }

    /**
     * Test to verify if a valid Visa card number is detected correctly.
     */
    @Test
    public void testValidateVisaCardNumber_Success() {
        boolean result = paymentProcessor.validateVisaCardNumber();
        assertTrue(result, "The Visa card number should be considered valid.");
    }
}
