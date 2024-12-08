package fr.unice.polytech.equipe.j.order.grouporder.backend;

import fr.unice.polytech.equipe.j.httpresponse.HttpCode;
import fr.unice.polytech.equipe.j.httpresponse.HttpResponse;
import fr.unice.polytech.equipe.j.order.grouporder.dto.CampusUserDTO;
import fr.unice.polytech.equipe.j.order.grouporder.dto.DeliveryDetailsDTO;
import fr.unice.polytech.equipe.j.order.grouporder.dto.OrderDTO;
import fr.unice.polytech.equipe.j.order.grouporder.dto.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
public class GroupOrderProxy implements IGroupOrder {
    private final IGroupOrder groupOrder;

    public GroupOrderProxy(IGroupOrder groupOrder) {
        this.groupOrder = groupOrder;
    }

    @Override
    public void setDeliveryTime(LocalDateTime deliveryTime) {
        if (groupOrder.getDeliveryDetails().getDeliveryTime().isPresent()) {
            throw new UnsupportedOperationException("You cannot change the delivery time of a group order.");
        }

        if (!isDeliveryTimeValid(deliveryTime)) {
            throw new UnsupportedOperationException("Invalid delivery time for one or more restaurants.");
        }

        groupOrder.setDeliveryTime(deliveryTime);
    }

    @Override
    public void addOrder(OrderDTO orderDTO) {
        if (groupOrder.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Cannot join a group order that is not pending");
        }
        if (!orderDTO.getRestaurant().canPrepareItemForGroupOrderDeliveryTime(this)) {
            throw new IllegalArgumentException("Order cannot be added, restaurant cannot prepare items in time");
        }
        // Check that all items inside can be prepared in time
        if (groupOrder.getDeliveryDetails().getDeliveryTime().isPresent() && !orderDTO.getRestaurant().canAccommodateDeliveryTime(orderDTO.getItems(), groupOrder.getDeliveryDetails().getDeliveryTime().get())) {
            throw new IllegalArgumentException("Cannot add order to group order, it will not be ready in time.");
        }
        // Check user is inside the group order
        if (!groupOrder.getUsers().contains(orderDTO.getUser())) {
            throw new IllegalArgumentException("Cannot add order to group order, user is not part of the group.");
        }
        groupOrder.addOrder(orderDTO);
    }

    private boolean isDeliveryTimeValid(LocalDateTime deliveryTime) {
        return groupOrder.getOrders().stream()
                .allMatch(order -> order.getRestaurant().canAccommodateDeliveryTime(order.getItems(), deliveryTime));
    }

    @Override
    public HttpResponse addUser(CampusUserDTO user) {
        if (groupOrder.getStatus() != OrderStatus.PENDING) {
            return new HttpResponse(HttpCode.HTTP_400, "Order cannot be added to groupOrder");
        }
        return groupOrder.addUser(user);

    }

    @Override
    public HttpResponse validate(CampusUserDTO user) {
        if (getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Cannot validate group order that is not pending.");
        }
        if (getDeliveryDetails().getDeliveryTime() == null) {
            throw new IllegalArgumentException("Cannot validate group order with no delivery time.");
        }
        if (!getUsers().contains(user)) {
            throw new IllegalArgumentException("Cannot validate group order if you are not part of it.");
        }
        // check if the user has an order, if not throw an exception
        if (getOrders().stream().noneMatch(order -> order.getUser().equals(user))) {
            throw new IllegalArgumentException("Cannot validate group order if you have no order.");
        }
        groupOrder.validate(user);
    }

    @Override
    public OrderStatus getStatus() {
        return groupOrder.getStatus();
    }

    @Override
    public void setStatus(OrderStatus status) {
        groupOrder.setStatus(status);
    }

    @Override
    public List<CampusUserDTO> getUsers() {
        return groupOrder.getUsers();
    }

    @Override
    public UUID getGroupOrderId() {
        return groupOrder.getGroupOrderId();
    }

    @Override
    public DeliveryDetailsDTO getDeliveryDetails() {
        return groupOrder.getDeliveryDetails();
    }

    @Override
    public List<OrderDTO> getOrders() {
        return groupOrder.getOrders();
    }
}
