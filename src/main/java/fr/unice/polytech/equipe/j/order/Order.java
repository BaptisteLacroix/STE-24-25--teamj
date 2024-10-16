package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    private final UUID orderUUID = UUID.randomUUID();
    private final Restaurant restaurant;
    private final List<MenuItem> items;
    private OrderStatus status;

    public Order(Restaurant restaurant) {
        this.restaurant = restaurant;
        items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void addItem(MenuItem item) {
        if (getStatus().equals(OrderStatus.VALIDATED)) {
            throw new IllegalStateException("Cannot add items to an order that is already in preparation.");
        }
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
