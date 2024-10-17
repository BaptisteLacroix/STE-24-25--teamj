package fr.unice.polytech.equipe.j.order;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GroupOrder {
    private final UUID groupOrderId;
    private final List<Order> orders = new ArrayList<>();
    private final DeliveryDetails deliveryDetails;
    private OrderStatus status = OrderStatus.PENDING;

    public GroupOrder(DeliveryDetails deliveryDetails) {
        this.groupOrderId = UUID.randomUUID();
        this.deliveryDetails = deliveryDetails;
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

    public DeliveryDetails getDeliveryDetails() {
        return deliveryDetails;
    }

    /**
     * If the user has defined the delivery time, it cannot be changed.
     *
     * @param deliveryTime The time when the order will be delivered
     */
    public void setDeliveryTime(LocalDateTime deliveryTime) {
        // TODO: Check if the delivery time is compatible with all sub orders
        if (getDeliveryDetails().getDeliveryTime().isPresent()) {
            throw new UnsupportedOperationException("You cannot change the delivery time of a group order.");
        }
        if (deliveryTime.isBefore(LocalDateTime.now())) {
            throw new UnsupportedOperationException("You cannot specify a delivery time in the past.");
        }
        deliveryDetails.setDeliveryTime(deliveryTime);
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderStatus getStatus() {
        return status;
    }

    @Override
    public String toString() {
        return "GroupOrder{" +
                "groupOrderId=" + groupOrderId +
                "deliveryDetails=" + deliveryDetails +
                ", orders=" + orders +
                '}';
    }
}
