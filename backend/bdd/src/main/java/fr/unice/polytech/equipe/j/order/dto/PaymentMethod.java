package fr.unice.polytech.equipe.j.order.dto;

public enum PaymentMethod {
    CREDIT_CARD("credit_card"),
    PAYPAL("paypal"),
    PAYLIB("paylib");

    private final String method;

    PaymentMethod(String method) {
        this.method = method;
    }
}
