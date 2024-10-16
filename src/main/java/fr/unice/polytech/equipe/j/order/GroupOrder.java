package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.user.ConnectedUser;

import java.time.LocalDateTime;
import java.util.*;

public class GroupOrder {
    private final UUID groupOrderId;
    private final Map<Order, ConnectedUser> ordersToConnectedUser = new HashMap<>();
    private final List<ConnectedUser> users = new ArrayList<>();
    private final DeliveryDetails deliveryDetails;
    private OrderStatus status = OrderStatus.PENDING;

    public GroupOrder(DeliveryDetails deliveryDetails) {
        this.groupOrderId = UUID.randomUUID();
        this.deliveryDetails = deliveryDetails;
    }

    public Map<Order, ConnectedUser> getOrdersToConnectedUser() {
        return ordersToConnectedUser;
    }

    // Add an individual order to the group
    public void addOrder(Order order, ConnectedUser user) {
        this.ordersToConnectedUser.put(order, user);
    }

    // Getters
    public UUID getGroupOrderId() {
        return groupOrderId;
    }

    /**
     * You can get the order as an unmodifiableList but please use addOrder to add an order
     */
    public List<Order> getOrders() {
        return List.copyOf(this.ordersToConnectedUser.keySet());
    }

    public DeliveryDetails getDeliveryDetails() {
        return deliveryDetails;
    }

    /**
     * If the user has defined the delivery time, it cannot be changed.
     * @param deliveryTime The time when the order will be delivered
     */
    public void setDeliveryTime(LocalDateTime deliveryTime) {
        if (getDeliveryDetails().getDeliveryTime() != null) {
            throw new UnsupportedOperationException("You cannot change the delivery time of a group order.");
        }
        if (deliveryTime.isBefore(LocalDateTime.now())) {
            throw new UnsupportedOperationException("You cannot specify a delivery time in the past.");
        }
        deliveryDetails.setDeliveryTime(deliveryTime);
    }

    @Override
    public String toString() {
        return "GroupOrder{" +
                "groupOrderId=" + groupOrderId +
                "deliveryDetails=" + deliveryDetails +
                ", orders=" + this.getOrders() +
                '}';
    }

    public OrderStatus getStatus() {
        return status;
    }
}
