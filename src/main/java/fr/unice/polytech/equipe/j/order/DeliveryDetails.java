package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.delivery.DeliveryLocation;
import fr.unice.polytech.equipe.j.delivery.DeliveryLocationManager;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;

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
        this.deliveryTime = Optional.ofNullable(deliveryTime);
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

    /**
     * Update the delivery time of the order, in case of a change in the preparation time of an item.
     * @param item
     */
    public void updateDeliveryTime(MenuItem item) {
        int prepTime = item.getPrepTime();
        if (deliveryTime.isPresent()) {
            deliveryTime = Optional.of(deliveryTime.get().plusMinutes(prepTime));
        }
        deliveryTime = Optional.of(LocalDateTime.now().plusMinutes(prepTime));
    }
}
