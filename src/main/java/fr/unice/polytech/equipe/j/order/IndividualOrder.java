package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;

public class IndividualOrder extends Order {
    private DeliveryDetails deliveryDetails;

    public IndividualOrder(Restaurant restaurantView, DeliveryDetails deliveryDetails) {
        super(restaurantView);
        this.deliveryDetails = deliveryDetails;
    }

    public DeliveryDetails getDeliveryDetails() {
        return deliveryDetails;
    }

    public void updateDeliveryTime(MenuItem item) {
        deliveryDetails.updateDeliveryTime(item);
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
