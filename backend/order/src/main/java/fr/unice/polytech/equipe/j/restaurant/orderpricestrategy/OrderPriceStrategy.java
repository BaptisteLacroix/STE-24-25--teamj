package fr.unice.polytech.equipe.j.restaurant.orderpricestrategy;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.restaurant.IRestaurant;

public interface OrderPriceStrategy {
    OrderPrice processOrderPrice(Order order, IRestaurant restaurant);
}
