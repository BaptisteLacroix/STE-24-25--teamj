package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderBuilder {
    private UUID restaurantUUID;
    private final List<MenuItem> items = new ArrayList<>();
    private LocalDateTime deliveryTime;
    private UUID orderId = UUID.randomUUID();

    /**
     * Set the restaurant for the order
     *
     * @param restaurant The restaurant to order from
     * @return The OrderBuilder instance
     */
    public OrderBuilder setRestaurantUUID(UUID restaurantUUID) {
        this.restaurantUUID = restaurantUUID;
        return this;
    }

    /**
     * Add a MenuItem to the order
     *
     * @param item The MenuItem to add
     * @return The OrderBuilder instance
     */
    public OrderBuilder addMenuItem(MenuItem item) {
        items.add(item);
        return this;
    }

    /**
     * Add a list of MenuItems to the order
     *
     * @param items The list of MenuItems to add
     * @return The OrderBuilder instance
     */
    public OrderBuilder addMenuItems(List<MenuItem> items) {
        this.items.addAll(items);
        return this;
    }

    /**
     * Set the delivery time for the order
     *
     * @param deliveryTime The delivery time
     * @return The OrderBuilder instance
     */
    public OrderBuilder setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
        return this;
    }

    /**
     * Build the order
     *
     * @return The Order instance
     */
    public Order build() {
        Order order = new Order(restaurantUUID, orderId);
        items.forEach(order::addItem);
        order.setDeliveryTime(deliveryTime);
        return order;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public UUID getRestaurantUUID() {
        return restaurantUUID;
    }
}
