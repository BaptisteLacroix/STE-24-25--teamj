package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.user.CampusUser;
import fr.unice.polytech.equipe.j.user.ConnectedUser;

import java.util.List;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Order {
    private final UUID orderUUID = UUID.randomUUID();
    private final Restaurant restaurant;
    private final List<MenuItem> items;
    private final CampusUser user;
    private OrderStatus status;
    private final Clock clock;

    public Order(Restaurant restaurant, Clock clock, CampusUser user) {
        this.restaurant = restaurant;
        items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.clock = clock;
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

    public Clock getClock() {
        return clock;
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
