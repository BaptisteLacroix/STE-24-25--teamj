package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.restaurant.RestaurantProxy;
import fr.unice.polytech.equipe.j.user.CampusUser;

public class IndividualOrder extends Order {
    private final DeliveryDetails deliveryDetails;

    public IndividualOrder(RestaurantProxy restaurant, DeliveryDetails deliveryDetails, CampusUser user) {
        super(restaurant, user);
        if (deliveryDetails.getDeliveryTime().isEmpty()) {
            throw new IllegalArgumentException("Cannot initiate order without delivery time.");
        }
        this.deliveryDetails = deliveryDetails;
//        this.setOnItemAdded(this::checkOrderUpdate);
    }

    // TODO: See later
//    private void checkOrderUpdate(MenuItem menuItem) {
//        // Check if the item can be prepared in time for the delivery
//        Optional<LocalDateTime> deliveryTime = this.deliveryDetails.getDeliveryTime();
//        LocalDateTime estimatedReadyTime = TimeUtils.getNow().plusSeconds(menuItem.getPrepTime());
//
//        if (deliveryTime.isPresent() && estimatedReadyTime.isAfter(deliveryTime.orElseThrow())){
//            throw new IllegalArgumentException("Cannot add item to order, it will not be ready in time.");
//        }
//
//        if (deliveryTime.isPresent() && !this.getRestaurant().slotAvailable(menuItem, deliveryTime.get())) {
//            throw new IllegalArgumentException("Cannot add item to order, no slot available.");
//        }
//    }

    public DeliveryDetails getDeliveryDetails() {
        return deliveryDetails;
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
}
