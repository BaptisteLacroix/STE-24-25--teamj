package fr.unice.polytech.equipe.j.order;

import java.time.LocalDateTime;
import java.util.Optional;

public class DeliveryDetails {
    private final String deliveryLocation;
    private Optional<LocalDateTime> deliveryTime;

    public DeliveryDetails(String deliveryLocation, LocalDateTime deliveryTime) {
        if (deliveryLocation == null) {
            throw new IllegalArgumentException("The delivery location must be specified.");
        }
        this.deliveryLocation = deliveryLocation;
        this.deliveryTime = Optional.ofNullable(deliveryTime);
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public Optional<LocalDateTime> getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = Optional.of(deliveryTime);
    }

    @Override
    public String toString() {
        return "DeliveryDetails{" +
                "deliveryLocation='" + deliveryLocation + '\'' +
                ", deliveryTime=" + deliveryTime +
                '}';
    }
}
