package fr.unice.polytech.equipe.j.order.dto;

public enum OrderStatus {
    PENDING("pending"),
    VALIDATED("validated"),
    DELIVERED("delivered"),
    CANCELLED("cancelled");

    private final String status;

    OrderStatus(String status) {
        this.status = status;
    }
}
