package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.user.ConnectedUser;

import java.util.UUID;

public class DeliverableOrder extends Order implements PriceableOrder{
    private DeliveryDetails deliveryDetails;

    public DeliverableOrder(Restaurant restaurant, UUID orderId, DeliveryDetails deliveryDetails, ConnectedUser user) {
        super(restaurant, orderId, user);
        this.deliveryDetails = deliveryDetails;
    }

    public DeliveryDetails getDeliveryDetails() {
        return deliveryDetails;
    }

    public void setDeliveryDetails(DeliveryDetails deliveryDetails) {
        this.deliveryDetails = deliveryDetails;
    }

    @Override
    public double processPrice() {
        return 0;
    }
}