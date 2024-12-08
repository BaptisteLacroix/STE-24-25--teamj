package fr.unice.polytech.equipe.j.payment.dto;

import fr.unice.polytech.equipe.j.payment.PaymentMethod;

public class PaymentRequestDTO {
    private String userId;
    private PaymentMethod paymentMethod;
    private Double amount;

    // Getters and setters
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}
