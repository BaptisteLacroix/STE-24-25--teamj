package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.user.ConnectedUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderBuilder {
    private Restaurant restaurant;
    private final List<MenuItem> items = new ArrayList<>();
    private final UUID orderId = UUID.randomUUID();

    public OrderBuilder setUser(ConnectedUser user) {
        this.user = user;
        return this;
    }

    private ConnectedUser user;

    /**
     * Set the restaurant for the order
     *
     * @param restaurant The restaurant to order from
     * @return The OrderBuilder instance
     */
    public OrderBuilder setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
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
     * Build the order
     *
     * @return The Order instance
     */
    public Order build() {
        Order order = new Order(restaurant, orderId, this.user);
        items.forEach(order::addItem);
        return order;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }
}
