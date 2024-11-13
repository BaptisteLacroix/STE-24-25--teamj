package fr.unice.polytech.equipe.j.grouporder.backend;

import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderStatus;
import fr.unice.polytech.equipe.j.user.CampusUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IGroupOrder {

    void addOrder(Order order);

    void addUser(CampusUser user);

    List<CampusUser> getUsers();

    UUID getGroupOrderId();

    List<Order> getOrders();

    DeliveryDetails getDeliveryDetails();

    void setDeliveryTime(LocalDateTime deliveryTime);

    OrderStatus getStatus();

    void setStatus(OrderStatus status);

    void validate(CampusUser user);

    String toString();
}
