package fr.unice.polytech.equipe.j.order;

import java.util.List;
import java.util.UUID;

public class GroupOrderFacade {
    private final UUID groupOrderId;
    private final List<Order> orders;
    private final DeliveryDetails deliveryDetails;
    private final OrderStatus status;

    public GroupOrderFacade(GroupOrder groupOrder) {
        this.groupOrderId = groupOrder.getGroupOrderId();
        this.orders = groupOrder.getOrders();
        this.deliveryDetails = groupOrder.getDeliveryDetails();
        this.status = groupOrder.getStatus();
    }

    public UUID getGroupOrderId() {
        return groupOrderId;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public DeliveryDetails getDeliveryDetails() {
        return deliveryDetails;
    }

    public OrderStatus getStatus() {
        return status;
    }
}
