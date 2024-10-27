package fr.unice.polytech.equipe.j.restaurant.strategies.order;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.restaurant.IRestaurant;

@FunctionalInterface
public interface OrderPriceStrategy {
    OrderPrice processOrderPrice(Order order, IRestaurant restaurant);
}
