package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.user.CampusUser;

import java.time.LocalDateTime;
import java.util.Optional;

public class IndividualOrder extends Order {
    private final DeliveryDetails deliveryDetails;

    public IndividualOrder(Restaurant restaurant, DeliveryDetails deliveryDetails, CampusUser user) {
        super(restaurant, user);
        this.deliveryDetails = deliveryDetails;
        this.setOnItemAdded(this::checkOrderUpdate);
    }

    private void checkOrderUpdate(MenuItem menuItem) {
        // Check if the item can be prepared in time for the delivery
        Optional<LocalDateTime> deliveryTime = this.deliveryDetails.getDeliveryTime();
        LocalDateTime estimatedReadyTime = TimeUtils.getNow().plusSeconds(menuItem.getPrepTime());

        if (deliveryTime.isPresent() && estimatedReadyTime.isAfter(deliveryTime.orElseThrow())){
            throw new IllegalArgumentException("Cannot add item to order, it will not be ready in time.");
        }

        if (deliveryTime.isPresent() && !this.getRestaurant().slotAvailable(menuItem, deliveryTime.get())) {
            throw new IllegalArgumentException("Cannot add item to order, no slot available.");
        }
    }

    public DeliveryDetails getDeliveryDetails() {
        return deliveryDetails;
    }

//    /**
//     * Sets the delivery time for an individual order. This validates that the time is compatible with the restaurant's operating hours and item prep time.
//     */
//    public void setDeliveryTime(Restaurant restaurant, LocalDateTime deliveryTime) {
//        // Check if the delivery time is in the past
//        if (deliveryTime.isBefore(TimeUtils.getNow())) {
//            throw new UnsupportedOperationException("You cannot specify a delivery time in the past.");
//        }
//
//        // Check if the restaurant is open
//        if (restaurant.getOpeningTime().isEmpty() || restaurant.getClosingTime().isEmpty()) {
//            throw new UnsupportedOperationException("The restaurant is not open at this time.");
//        }
//
//        // Check if the restaurant is open at the specified delivery time
//        if (deliveryTime.isBefore(restaurant.getOpeningTime().get()) || deliveryTime.isAfter(restaurant.getClosingTime().get())) {
//            throw new UnsupportedOperationException("The restaurant is closed at this time.");
//        }
//
//        // Check if the delivery time is compatible with the preparation time of the items
//        LocalDateTime earliestPossibleDeliveryTime = TimeUtils.getNow().plusSeconds(restaurant.getPreparationTime(getItems()));
//        if (deliveryTime.isBefore(earliestPossibleDeliveryTime)) {
//            throw new UnsupportedOperationException("The delivery time is too soon.");
//        }
//
//        // Set the delivery time if all validations pass
//        deliveryDetails.setDeliveryTime(deliveryTime);
//    }

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
}
