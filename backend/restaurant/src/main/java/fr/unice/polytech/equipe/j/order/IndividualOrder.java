package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.restaurant.IRestaurant;
import fr.unice.polytech.equipe.j.user.CampusUser;

public class IndividualOrder extends Order {
    private final DeliveryDetails deliveryDetails;

    public IndividualOrder(IRestaurant restaurant, DeliveryDetails deliveryDetails, CampusUser user) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IndividualOrder that)) return false;
        if (!super.equals(o)) return false;
        return getDeliveryDetails().equals(that.getDeliveryDetails());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getDeliveryDetails().hashCode();
        return result;
    }
}
