package fr.unice.polytech.equipe.j.payment;

/**
 * Processes PayPal payments.
 */
public class PayPalProcessor implements PaymentProcessor {

    /**
     * Processes a PayPal payment of the specified amount.
     * Simulates payment processing logic.
     *
     * @param amount the amount to be processed
     * @return true if the payment was successful; false otherwise
     */
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing PayPal payment of €" + amount);
        // Simulate payment processing logic
        if (amount <= 1000) {
            return true;
        } else {
            System.out.println("PayPal payment failed: Amount exceeds limit.");
            return false;
        }
    }
}
