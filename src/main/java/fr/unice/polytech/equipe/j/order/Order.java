package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.MenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    private final UUID orderUUID;
    private final UUID restaurantUUID;
    private final List<MenuItem> items;
    private LocalDateTime deliveryTime;
    private OrderStatus status;

    public Order(UUID restaurantUUID, UUID orderUUID) {
        this.restaurantUUID = restaurantUUID;
        items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.orderUUID = orderUUID;
    }

    public UUID getRestaurantUUID() {
        return restaurantUUID;
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
        if (getStatus().equals(OrderStatus.IN_PREPARATION)) {
            throw new IllegalStateException("Cannot add items to an order that is already in preparation.");
        }
        items.add(item);
    }

    public void removeItem(MenuItem item) {
        if (getStatus().equals(OrderStatus.IN_PREPARATION)) {
            throw new IllegalStateException("Cannot remove items from an order that is already in preparation.");
        }
        items.remove(item);
    }

    /**
     * Calculate the total price of the order
     * @return The total price of the order
     */
    public double getTotalPrice() {
        return RestaurantManager.calculateOrderPriceFromRestaurant(this);
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
                ", restaurantUUID=" + restaurantUUID +
                ", items=" + items +
                ", totalPrice=" + getTotalPrice() +
                ", status=" + status +
                ", deliveryTime=" + deliveryTime +
                '}';
    }
}
