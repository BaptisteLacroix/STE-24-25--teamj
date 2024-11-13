package fr.unice.polytech.equipe.j.payment.strategy;

/**
 * The PaymentProcessor interface defines the contract for processing payments.
 */
public interface PaymentProcessor {
    /**
     * Processes a payment of the specified amount.
     *
     * @param amount the amount to be processed
     * @return true if the payment was processed successfully; false otherwise
     */
    boolean processPayment(double amount);
}
