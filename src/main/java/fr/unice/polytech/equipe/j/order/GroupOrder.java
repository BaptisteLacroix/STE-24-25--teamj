package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.user.ConnectedUser;
import fr.unice.polytech.equipe.j.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GroupOrder {
    private final UUID groupOrderId;
    private final List<Order> orders = new ArrayList<>();
    private final List<ConnectedUser> users = new ArrayList<>();
    private final String deliveryLocation;
    private LocalDateTime deliveryTime;
    private OrderStatus status = OrderStatus.PENDING;

    public GroupOrder(String deliveryLocation, LocalDateTime deliveryTime) {
        this.groupOrderId = UUID.randomUUID();
        this.deliveryLocation = deliveryLocation;
        this.deliveryTime = deliveryTime;
    }

    public GroupOrder(String deliveryLocation) {
        this.groupOrderId = UUID.randomUUID();
        this.deliveryLocation = deliveryLocation;
        this.deliveryTime = null;
    }

    // Add an individual order to the group
    public void addOrder(Order order) {
        orders.add(order);
    }

    // Getters
    public UUID getGroupOrderId() {
        return groupOrderId;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public String getDeliveryLocation() {
        return deliveryLocation;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        if (deliveryTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Delivery time must be in the future.");
        }
        if (getDeliveryTime() != null) {
            throw new IllegalArgumentException("Delivery time must be specified only one time.");
        }
        this.deliveryTime = deliveryTime;
    }

    @Override
    public String toString() {
        return "GroupOrder{" +
                "groupOrderId=" + groupOrderId +
                ", deliveryLocation='" + deliveryLocation + '\'' +
                ", deliveryTime=" + deliveryTime +
                ", orders=" + orders +
                '}';
    }
}
