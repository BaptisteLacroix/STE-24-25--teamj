package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.Order;

import java.time.LocalDateTime;
import java.util.Optional;

public interface IRestaurant {

    /**
     * Add an item to an order, either individual or group.
     *
     * @param order        the order to which the item is added
     * @param menuItem     the menu item being added
     * @param deliveryTime the delivery time for the order (optional)
     * @throws IllegalArgumentException if the item is not available or cannot be prepared in time
     */
    void addItemToOrder(Order order, MenuItem menuItem, LocalDateTime deliveryTime);

    void cancelOrder(Order order);

    void onOrderPaid(Order order);

    double getTotalPrice(Order order);

    Optional<LocalDateTime> getOpeningTime();

    Optional<LocalDateTime> getClosingTime();

    boolean isSlotCapacityAvailable();
}
