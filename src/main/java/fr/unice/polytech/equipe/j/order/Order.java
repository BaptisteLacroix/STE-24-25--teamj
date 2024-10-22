package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.user.CampusUser;

import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.function.Consumer;

public class Order {
    private final UUID orderUUID = UUID.randomUUID();
    private final Restaurant restaurant;
    private final List<MenuItem> items;
    private final CampusUser user;
    private OrderStatus status;
    private Consumer<MenuItem> onItemAdded = (item)->{};

    public void setOnItemAdded(Consumer<MenuItem> onItemAdded) {
        this.onItemAdded = onItemAdded;
    }


    public Order(Restaurant restaurant, CampusUser user) {
        this.restaurant = restaurant;
        items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.user = user;
    }

    public CampusUser getUser() {
        return user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void addItem(MenuItem item) {
        if (getStatus().equals(OrderStatus.VALIDATED))
            throw new IllegalStateException("Cannot add items to an order that is already in preparation.");
        if (!restaurant.isItemAvailable(item))
            throw new IllegalArgumentException("Item is not available.");

        // call item listener
        this.onItemAdded.accept(item);
        items.add(item);
    }

    public void removeItem(MenuItem item) {
        if (getStatus().equals(OrderStatus.VALIDATED)) {
            throw new IllegalStateException("Cannot remove items from an order that is already in preparation.");
        }
        items.remove(item);
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public UUID getOrderUUID() {
        return orderUUID;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderUUID=" + orderUUID +
                ", restaurantUUID=" + restaurant +
                ", items=" + items +
                ", status=" + status +
                '}';
    }
}
