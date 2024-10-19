package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.user.ConnectedUser;

import java.util.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

public class Order {
    private final UUID orderId;
    private final Restaurant restaurant;
    private final List<MenuItem> items;
    private final ConnectedUser user;
    private OrderStatus status;

    public Order(Restaurant restaurant, UUID orderId, ConnectedUser user) {
        this.restaurant = restaurant;
        items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.orderId = orderId;
        this.user = user;
    }

    public ConnectedUser getUser() {
        return user;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public void addItem(MenuItem item) {
        items.add(item);
    }

    public void removeItem(MenuItem item) {
        items.remove(item);
    }

    // This method now calls the restaurant to calculate the total price of the order
    public double getTotalPrice() {
        return restaurant.calculatePrice(this);
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public UUID getOrderId() {
        return orderId;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", restaurant=" + restaurant +
                ", items=" + items +
                ", totalPrice=" + getTotalPrice() +
                ", status=" + status +
                '}';
    }
}
