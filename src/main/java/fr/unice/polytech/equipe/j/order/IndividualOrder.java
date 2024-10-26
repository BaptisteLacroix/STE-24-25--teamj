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
    }

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
