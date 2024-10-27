package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.user.CampusUser;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class GroupOrderProxy {
    private final GroupOrder groupOrder;

    public GroupOrderProxy(GroupOrder groupOrder) {
        this.groupOrder = groupOrder;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        if (groupOrder.getDeliveryDetails().getDeliveryTime().isPresent()) {
            throw new UnsupportedOperationException("You cannot change the delivery time of a group order.");
        }

        if (!isDeliveryTimeValid(deliveryTime)) {
            throw new UnsupportedOperationException("Invalid delivery time for one or more restaurants.");
        }

        groupOrder.setDeliveryTime(deliveryTime);
    }

    public void addOrder(Order order) {
        if (groupOrder.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Cannot join a group order that is not pending");
        }
        if (!order.getRestaurant().canPrepareItemForGroupOrderDeliveryTime(this)) {
            throw new IllegalArgumentException("Order cannot be added, restaurant cannot prepare items in time");
        }
        // Check that all items inside can be prepared in time
        if (groupOrder.getDeliveryDetails().getDeliveryTime().isPresent() && !order.getRestaurant().canAccommodateDeliveryTime(order.getItems(), groupOrder.getDeliveryDetails().getDeliveryTime().get())) {
            throw new IllegalArgumentException("Cannot add order to group order, it will not be ready in time.");
        }
        // Check user is inside the group order
        if (!groupOrder.getUsers().contains(order.getUser())) {
            throw new IllegalArgumentException("Cannot add order to group order, user is not part of the group.");
        }
        groupOrder.addOrder(order);
    }

    private boolean isDeliveryTimeValid(LocalDateTime deliveryTime) {
        return groupOrder.getOrders().stream()
                .allMatch(order -> order.getRestaurant().canAccommodateDeliveryTime(order.getItems(), deliveryTime));
    }

    public void addUser(CampusUser user) {
        if (groupOrder.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("The group order has already been validated.");
        }
        groupOrder.addUser(user);
    }

    public void validate(CampusUser user) {
        if (groupOrder.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Cannot validate group order that is not pending.");
        }
        if (groupOrder.getDeliveryDetails().getDeliveryTime().isEmpty()) {
            throw new IllegalArgumentException("Cannot validate group order with no delivery time.");
        }
        if (!groupOrder.getUsers().contains(user)) {
            throw new IllegalArgumentException("Cannot validate group order if you are not part of it.");
        }
        // check if the user has an order, if not throw an exception
        if (groupOrder.getOrders().stream().noneMatch(order -> order.getUser().equals(user))) {
            throw new IllegalArgumentException("Cannot validate group order if you have no order.");
        }
        groupOrder.setStatus(OrderStatus.VALIDATED);
    }

    public OrderStatus getStatus() {
        return groupOrder.getStatus();
    }

    public List<CampusUser> getUsers() {
        return groupOrder.getUsers();
    }

    public UUID getGroupOrderId() {
        return groupOrder.getGroupOrderId();
    }

    // Delegate other GroupOrder methods
    public DeliveryDetails getDeliveryDetails() {
        return groupOrder.getDeliveryDetails();
    }

    public List<Order> getOrders() {
        return groupOrder.getOrders();
    }
}
