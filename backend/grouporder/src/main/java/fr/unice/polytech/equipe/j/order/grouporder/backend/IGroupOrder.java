package fr.unice.polytech.equipe.j.order.grouporder.backend;

import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.order.grouporder.dto.OrderDTO;
import fr.unice.polytech.equipe.j.order.grouporder.dto.CampusUserDTO;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IGroupOrder {

    void addOrder(OrderDTO order);

    HttpResponse addUser(CampusUserDTO user);

    List<CampusUserDTO> getUsers();

    UUID getGroupOrderId();

    HttpResponse<String> getOrders();

    DeliveryDetails getDeliveryDetails();

    void setDeliveryTime(LocalDateTime deliveryTime);

    OrderStatus getStatus();

    void setStatus(OrderStatus status);

    void validate(CampusUserDTO user);

    String toString();
}
