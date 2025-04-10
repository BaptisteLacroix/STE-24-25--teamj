package fr.unice.polytech.equipe.j.backend;

import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.dto.DeliveryDetailsDTO;
import fr.unice.polytech.equipe.j.dto.OrderDTO;
import fr.unice.polytech.equipe.j.dto.CampusUserDTO;
import fr.unice.polytech.equipe.j.dto.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface IGroupOrder {

    void addOrder(OrderDTO order);

    HttpResponse addUser(CampusUserDTO user);

    List<CampusUserDTO> getUsers();

    UUID getGroupOrderId();

    List<OrderDTO> getOrders();

    DeliveryDetailsDTO getDeliveryDetails();

    void setDeliveryTime(LocalDateTime deliveryTime);

    OrderStatus getStatus();

    void setStatus(OrderStatus status);

    HttpResponse validate(CampusUserDTO user);

    String toString();
}
