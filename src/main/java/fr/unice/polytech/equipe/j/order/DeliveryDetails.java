package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;

import java.time.LocalDateTime;

public class DeliveryDetails {
    private final DeliveryLocation deliveryLocation;
    private LocalDateTime deliveryTime;

    public DeliveryDetails(DeliveryLocation deliveryLocation, LocalDateTime deliveryTime) {
        this.deliveryLocation = deliveryLocation;
        this.deliveryTime = deliveryTime;
    }

    public DeliveryLocation getDeliveryLocation() {
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
