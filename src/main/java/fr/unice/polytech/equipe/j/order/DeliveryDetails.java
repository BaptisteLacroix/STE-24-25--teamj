package fr.unice.polytech.equipe.j.order;

import java.time.LocalDateTime;

public class DeliveryDetails {
    private final String deliveryLocation;
    private LocalDateTime deliveryTime;

    public DeliveryDetails(String deliveryLocation, LocalDateTime deliveryTime) {
        this.deliveryLocation = deliveryLocation;
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    @Override
    public String toString() {
        return "DeliveryDetails{" +
                "deliveryLocation='" + deliveryLocation + '\'' +
                ", deliveryTime=" + deliveryTime +
                '}';
    }
}
