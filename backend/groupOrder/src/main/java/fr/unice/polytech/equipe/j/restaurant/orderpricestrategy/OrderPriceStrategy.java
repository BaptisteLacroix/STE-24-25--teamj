package java.fr.unice.polytech.equipe.j.restaurant.orderpricestrategy;

import java.fr.unice.polytech.equipe.j.order.Order;
import java.fr.unice.polytech.equipe.j.restaurant.IRestaurant;

public interface OrderPriceStrategy {
    OrderPrice processOrderPrice(Order order, IRestaurant restaurant);
}
