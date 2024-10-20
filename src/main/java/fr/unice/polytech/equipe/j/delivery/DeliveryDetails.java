package fr.unice.polytech.equipe.j.delivery;

import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;

import java.time.LocalDateTime;
import java.util.Optional;

public class DeliveryDetails {
    private final DeliveryLocation deliveryLocation;
    private Optional<LocalDateTime> deliveryTime;

    public DeliveryDetails(DeliveryLocation deliveryLocation, LocalDateTime deliveryTime) {
        if (deliveryLocation == null) {
            throw new IllegalArgumentException("The delivery location must be specified.");
        }
        // Check that the location exists
        DeliveryLocationManager.getInstance().findLocationByName(deliveryLocation.locationName());
        this.deliveryLocation = deliveryLocation;
        this.deliveryTime = deliveryTime == null ? Optional.empty() : Optional.of(deliveryTime);
    }

    public DeliveryLocation getDeliveryLocation() {
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
