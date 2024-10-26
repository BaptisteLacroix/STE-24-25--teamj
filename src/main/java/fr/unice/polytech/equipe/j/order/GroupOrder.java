package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.user.CampusUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GroupOrder {
    private final UUID groupOrderId;
    private final List<Order> orders = new ArrayList<>();
    private final List<CampusUser> users = new ArrayList<>();
    private final DeliveryDetails deliveryDetails;
    private OrderStatus status = OrderStatus.PENDING;

    public List<CampusUser> getUsers() {
        return users;
    }

    public void addUser(CampusUser user) {
        this.users.add(user);
    }

    public GroupOrder(DeliveryDetails deliveryDetails) {
        this.groupOrderId = UUID.randomUUID();
        this.deliveryDetails = deliveryDetails;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }


    // Getters
    public UUID getGroupOrderId() {
        return groupOrderId;
    }

    public List<Order> getOrders() {
        return this.orders;
    }

    public DeliveryDetails getDeliveryDetails() {
        return deliveryDetails;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        deliveryDetails.setDeliveryTime(deliveryTime);
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
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
