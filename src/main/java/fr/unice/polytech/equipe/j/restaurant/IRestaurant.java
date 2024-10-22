package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.order.Order;

public interface IRestaurant {
    void addOrder(Order order, DeliveryDetails deliveryDetails);

    public void cancelOrder(Order order);

    void orderPaid(Order order);
}
