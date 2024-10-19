package fr.unice.polytech.equipe.j.payment;

import fr.unice.polytech.equipe.j.user.CampusUser;

import java.time.LocalDateTime;

/**
 * Represents a payment transaction with details such as amount, payment method, and timestamp.
 */
public class Transaction {
    private final double amount;
    private final String paymentMethod;
    private final LocalDateTime timestamp;
    CampusUser user;

    /**
     * Constructs a new Transaction with the specified amount, payment method, and timestamp.
     *
     * @param amount        the transaction amount
     * @param paymentMethod the payment method used
     * @param timestamp     the date and time when the transaction occurred
     */
    public Transaction(double amount, String paymentMethod, LocalDateTime timestamp) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.timestamp = timestamp;
    }

    /**
     * Returns the transaction amount.
     *
     * @return the amount of the transaction
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returns the payment method used for the transaction.
     *
     * @return the payment method as a String
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * Returns the timestamp when the transaction occurred.
     *
     * @return the LocalDateTime of the transaction
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Returns a string representation of the transaction.
     *
     * @return a String containing transaction details
     */
    @Override
    public String toString() {
        return "Transaction{" +
               "amount=" + amount +
               ", paymentMethod='" + paymentMethod + '\'' +
               ", timestamp=" + timestamp +
               '}';
    }
}
