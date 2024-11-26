package java.fr.unice.polytech.equipe.j.order.grouporder.backend;

import java.fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import java.fr.unice.polytech.equipe.j.order.Order;
import java.fr.unice.polytech.equipe.j.order.OrderStatus;
import java.fr.unice.polytech.equipe.j.user.CampusUser;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class GroupOrder implements IGroupOrder {
    private final UUID groupOrderId;
    private final List<Order> orders = new ArrayList<>();
    private final List<CampusUser> users = new ArrayList<>();
    private final DeliveryDetails deliveryDetails;
    private OrderStatus status = OrderStatus.PENDING;

    @Override
    public List<CampusUser> getUsers() {
        return users;
    }

    @Override
    public void addUser(CampusUser user) {
        this.users.add(user);
    }

    public GroupOrder(DeliveryDetails deliveryDetails) {
        this.groupOrderId = UUID.randomUUID();
        this.deliveryDetails = deliveryDetails;
    }

    @Override
    public void addOrder(Order order) {
        this.orders.add(order);
    }

    @Override
    public void validate(CampusUser user) {
        setStatus(OrderStatus.VALIDATED);
    }

    // Getters
    @Override
    public UUID getGroupOrderId() {
        return groupOrderId;
    }

    @Override
    public List<Order> getOrders() {
        return this.orders;
    }

    @Override
    public DeliveryDetails getDeliveryDetails() {
        return deliveryDetails;
    }

    @Override
    public void setDeliveryTime(LocalDateTime deliveryTime) {
        deliveryDetails.setDeliveryTime(deliveryTime);
    }

    @Override
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

    @Override
    public OrderStatus getStatus() {
        return status;
    }
}
