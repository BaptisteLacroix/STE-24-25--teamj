package fr.unice.polytech.equipe.j.restaurant.backend.orderpricestrategy;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.restaurant.backend.IRestaurant;

public interface OrderPriceStrategy {
    OrderPrice processOrderPrice(Order order, IRestaurant restaurant);
}
