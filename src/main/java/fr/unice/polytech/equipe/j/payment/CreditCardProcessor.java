package fr.unice.polytech.equipe.j.payment;

/**
 * Processes credit card payments.
 */
public class CreditCardProcessor implements PaymentProcessor {

    /**
     * Processes a credit card payment of the specified amount.
     * Simulates payment processing logic.
     *
     * @param amount the amount to be processed
     * @return true if the payment was successful; false otherwise
     */
    @Override
    public void processPayment(double amount) {
        System.out.println("Processing credit card payment of €" + amount);
        // Simulate payment processing logic
        if (amount <= 500) {
            // Payment successful
            System.out.println("Credit card payment successful.");
        } else {
            throw new IllegalArgumentException("Credit card payment failed: Amount exceeds limit.");
        }
    }
}
