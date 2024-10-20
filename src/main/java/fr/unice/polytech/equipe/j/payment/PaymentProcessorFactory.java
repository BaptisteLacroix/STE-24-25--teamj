package fr.unice.polytech.equipe.j.payment;

/**
 * Factory class for creating PaymentProcessor instances based on PaymentMethod.
 */
public class PaymentProcessorFactory {

    /**
     * Creates a PaymentProcessor based on the specified PaymentMethod.
     *
     * @param method the payment method
     * @return a PaymentProcessor instance
     */
    public static PaymentProcessor createPaymentProcessor(PaymentMethod method) {
        if (method == null){
            throw new IllegalArgumentException("Invalid payment method: " + method);
        }
        switch (method) {
            case CREDIT_CARD:
                return new CreditCardProcessor();
            case PAYPAL:
                return new PayPalProcessor();
            case PAYLIB:
                return new PaylibProcessor();
            default:
                throw new IllegalArgumentException("Invalid payment method: " + method);
        }
    }
}
