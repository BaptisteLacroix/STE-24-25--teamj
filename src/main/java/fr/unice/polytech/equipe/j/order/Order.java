package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Order {
    private final UUID orderId = UUID.randomUUID();
    private final Restaurant restaurant;
    private final List<MenuItem> items;
    private LocalDateTime deliveryTime;
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

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
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
                ", deliveryTime=" + deliveryTime +
                '}';
    }
}
