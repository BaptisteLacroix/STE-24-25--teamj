package fr.unice.polytech.equipe.j.payment;

import java.time.LocalDateTime;

/**
 * The PaymentProcessor class is responsible for handling and processing payment transactions.
 * It manages payment details, validates the payment information, processes payments,
 * and supportss functionalities like refunding and canceling payments si besoin
 */
public class PaymentProcessor {

    /** Unique transaction ID for each payment */
    private String transactionId;

    /** The amount to be processed for the campusUser */
    private double amount;

    /** The method of payment (par ex, credit card, PayPal) */
    private String paymentMethod;

    /** The currency in which the payment is made */
    private String currency;

    /** The status of the payment ( PENDING, PROCESSED, FAILED) */
    private String status;

    /** The date and time when the payment was initiated */
    private LocalDateTime paymentDate;

    /** The customer identifier associated with this payment */
    private String customerId;

    /** The Visa card number used for payment */
    private String visaCardNumber;

    /**
     * Constructs a new PaymentProcessor object with the specified payment details.
     *
     * @param amount        The amount to be processed
     * @param paymentMethod The method of payment ( credit card, PayPal)
     * @param currency      The currency of the payment
     * @param customerId    The customer making the payment
     * @param visaCardNumber The Visa card number for the transaction
     */
    public PaymentProcessor(double amount, String paymentMethod, String currency, String customerId, String visaCardNumber) {
        this.amount = amount;
        this.paymentMethod = paymentMethod;
        this.currency = currency;
        this.customerId = customerId;
        this.visaCardNumber = visaCardNumber;
        this.transactionId = generateTransactionId();
        this.status = "PENDING";
        this.paymentDate = LocalDateTime.now();
    }

    /**
     * Processes the payment. This method validates the Visa card number and payment details
     * before processing the payment.
     *
     * @return true if the payment is processed successfully, false otherwise
     */
    public boolean processPayment() {
        if (validateVisaCardNumber() && validatePaymentDetails()) {
            // Process the payment based on method and amount
            this.status = "PROCESSED";
            logTransaction();
            return true;
        } else {
            this.status = "FAILED";
            return false;
        }
    }

    /**
     * Validates the Visa card number to ensure that it is a valid 16-digit Visa card.
     *
     * @return true if the Visa card number is valid, false otherwise
     */
    public boolean validateVisaCardNumber() {
        if (visaCardNumber == null || !visaCardNumber.matches("^4[0-9]{15}$")) {
            return false; // Visa card numbers start with 4 and are 16 digits long
        }
        return true;
    }

    /**
     * Validates the payment details based on the payment method and other relevant information.
     *
     * @return true if the payment details are valid, false otherwise
     */
    public boolean validatePaymentDetails() {
        // Add additional validation logic based on paymentMethod if needed
        return paymentMethod != null && !paymentMethod.isEmpty();
    }

    /**
     * Cancels a payment that is still pending. Payments that are already processed cannot be canceled.
     *
     * @return true if the payment was successfully canceled, false otherwise
     */
    public boolean cancelPayment() {
        if (this.status.equals("PENDING")) {
            this.status = "CANCELED";
            return true;
        }
        return false;
    }

    /**
     * Refunds a payment that has been processed successfully.
     *
     * @return true if the payment is successfully refunded, false otherwise
     */
    public boolean refundPayment() {
        if (this.status.equals("PROCESSED")) {
            this.status = "REFUNDED";
            return true;
        }
        return false;
    }

    /**
     * Returns the current status of the payment (e.g., PENDING, PROCESSED, FAILED).
     *
     * @return The current payment status
     */
    public String getPaymentStatus() {
        return this.status;
    }

    /**
     * Sets the payment method for this transaction (e.g., credit card, PayPal).
     *
     * @param paymentMethod The new payment method
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     * Returns the unique transaction ID associated with this payment.
     *
     * @return The transaction ID
     */
    public String getTransactionId() {
        return this.transactionId;
    }

    /**
     * Generates a unique transaction ID for the payment.
     * The transaction ID is typically based on the current time.
     *
     * @return A unique transaction ID
     */
    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis(); // Simple example of generating unique transaction ID
    }

    /**
     * Logs the payment transaction for record-keeping purposes.
     * The details of the transaction (e.g., amount, payment method) are stored.
     */
    public void logTransaction() {
        // Log transaction details to a file or database
        System.out.println("Transaction logged: " + this.transactionId);
    }
}
