package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.RestaurantFacade;

import java.util.UUID;

public class IndividualOrder extends Order {
    private DeliveryDetails deliveryDetails;

    public IndividualOrder(RestaurantFacade restaurantFacade, UUID orderUUID, DeliveryDetails deliveryDetails) {
        super(restaurantFacade, orderUUID);
        this.deliveryDetails = deliveryDetails;
    }

    public DeliveryDetails getDeliveryDetails() {
        return deliveryDetails;
    }

    public void setDeliveryDetails(DeliveryDetails deliveryDetails) {
        if (deliveryDetails == null) {
            throw new IllegalArgumentException("Delivery time cannot be null for an individual order.");
        }
        this.deliveryDetails = deliveryDetails;
    }

    @Override
    public String toString() {
        return "IndividualOrder{" +
                "orderId=" + getOrderUUID() +
                ", restaurant=" + getRestaurantFacade() +
                ", items=" + getItems() +
                ", deliveryTime=" + getDeliveryDetails() +
                ", totalPrice=" + getTotalPrice() +
                ", status=" + getStatus() +
                '}';
    }
}
