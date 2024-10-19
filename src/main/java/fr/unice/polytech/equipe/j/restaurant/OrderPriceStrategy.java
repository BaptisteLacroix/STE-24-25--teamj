package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.user.ConnectedUser;

@FunctionalInterface
public interface OrderPriceStrategy {
    OrderPrice processOrderPrice(Order order, Restaurant restaurant);
}
