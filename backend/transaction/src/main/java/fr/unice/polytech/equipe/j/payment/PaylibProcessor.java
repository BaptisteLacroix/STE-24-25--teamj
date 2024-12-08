package fr.unice.polytech.equipe.j.payment;

/**
 * Processes Paylib payments.
 */
public class PaylibProcessor implements PaymentProcessor {

    /**
     * Processes a Paylib payment of the specified amount.
     * Simulates payment processing logic.
     *
     * @param amount the amount to be processed
     * @return true if the payment was successful; false otherwise
     */
    @Override
    public boolean processPayment(double amount) {
        System.out.println("Processing Paylib payment of €" + amount);
        // Simulate payment processing logic
        if (amount <= 2000) {
            System.out.println("Paylib payment successful.");
            return true;
        } else {
            throw new IllegalArgumentException("Paylib payment failed: Amount exceeds limit.");
        }
    }
}
