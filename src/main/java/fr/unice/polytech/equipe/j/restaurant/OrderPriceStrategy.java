package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.Order;

@FunctionalInterface
public interface OrderPriceStrategy {
    OrderPrice processOrderPrice(Order order, IRestaurant restaurant);
}
