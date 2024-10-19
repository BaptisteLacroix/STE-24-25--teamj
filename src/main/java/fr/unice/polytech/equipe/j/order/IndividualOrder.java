package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

public class IndividualOrder extends Order {
    private DeliveryDetails deliveryDetails;

    public IndividualOrder(Restaurant restaurantView, DeliveryDetails deliveryDetails, Clock clock) {
        super(restaurantView, clock);
        this.deliveryDetails = deliveryDetails;
    }

    public DeliveryDetails getDeliveryDetails() {
        return deliveryDetails;
    }

    /**
     * Sets the delivery time for an individual order. This validates that the time is compatible with the restaurant's operating hours and item prep time.
     */
    public void setDeliveryTime(Restaurant restaurant, LocalDateTime deliveryTime) {
        // Check if the delivery time is in the past
        if (deliveryTime.isBefore(LocalDateTime.now(getClock()))) {
            throw new UnsupportedOperationException("You cannot specify a delivery time in the past.");
        }

        // Check if the restaurant is open at the specified delivery time
        if (deliveryTime.isBefore(restaurant.getOpeningTime()) || deliveryTime.isAfter(restaurant.getClosingTime())) {
            throw new UnsupportedOperationException("The restaurant is closed at this time.");
        }

        // Check if the delivery time is compatible with the preparation time of the items
        LocalDateTime earliestPossibleDeliveryTime = LocalDateTime.now(getClock()).plusSeconds(restaurant.getPreparationTime(getItems()));
        if (deliveryTime.isBefore(earliestPossibleDeliveryTime)) {
            throw new UnsupportedOperationException("The delivery time is too soon.");
        }

        // Set the delivery time if all validations pass
        deliveryDetails.setDeliveryTime(deliveryTime);
    }

    @Override
    public String toString() {
        return "IndividualOrder{" +
                "orderId=" + getOrderUUID() +
                ", restaurant=" + getRestaurant() +
                ", items=" + getItems() +
                ", deliveryTime=" + getDeliveryDetails() +
                ", status=" + getStatus() +
                '}';
    }

    public Optional<LocalDateTime> getPossibleDeliveryTime() {
        // If the delivery tim is not set return the latest delivery time possible from the order items
        if (deliveryDetails.getDeliveryTime().isEmpty()) {
            Optional<LocalDateTime> latestDeliveryTime = Optional.empty();
            for (MenuItem item : getItems()) {
                int preparationTime = item.getPrepTime();
                LocalDateTime orderDeliveryTime = LocalDateTime.now(getClock()).plusSeconds(preparationTime);
                if (latestDeliveryTime.isEmpty() || orderDeliveryTime.isAfter(latestDeliveryTime.get())) {
                    latestDeliveryTime = Optional.of(orderDeliveryTime);
                }
            }
            return latestDeliveryTime;
        }
        return deliveryDetails.getDeliveryTime();
    }
}
