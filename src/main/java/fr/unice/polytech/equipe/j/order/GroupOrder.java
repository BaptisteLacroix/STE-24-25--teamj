package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GroupOrder {
    private final UUID groupOrderId;
    private final List<Order> orders = new ArrayList<>();
    private final DeliveryDetails deliveryDetails;
    private OrderStatus status = OrderStatus.PENDING;
    private final Clock clock;

    public GroupOrder(DeliveryDetails deliveryDetails, Clock clock) {
        this.groupOrderId = UUID.randomUUID();
        this.deliveryDetails = deliveryDetails;
        this.clock = clock;
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

    public Optional<LocalDateTime> getPossibleDeliveryTime() {
        // If the delivery tim is not set return the latest delivery time possible from the orders items
        if (deliveryDetails.getDeliveryTime().isEmpty()) {
            Optional<LocalDateTime> latestDeliveryTime = Optional.empty();
            for (Order order : orders) {
                int preparationTime = order.getRestaurant().getPreparationTime(order.getItems());
                LocalDateTime orderDeliveryTime = LocalDateTime.now(clock).plusSeconds(preparationTime);
                if (latestDeliveryTime.isEmpty() || orderDeliveryTime.isAfter(latestDeliveryTime.get())) {
                    latestDeliveryTime = Optional.of(orderDeliveryTime);
                }
            }
            return latestDeliveryTime;
        }
        return deliveryDetails.getDeliveryTime();
    }

    /**
     * If the user has defined the delivery time, it cannot be changed.
     *
     * @param deliveryTime The time when the order will be delivered
     */
    public void setDeliveryTime(LocalDateTime deliveryTime) {
        if (getDeliveryDetails().getDeliveryTime().isPresent()) {
            throw new UnsupportedOperationException("You cannot change the delivery time of a group order.");
        }
        // check that all restaurants are open
        for (Order order : getOrders()) {
            if (order.getRestaurant().getOpeningTime().isEmpty() || order.getRestaurant().getClosingTime().isEmpty()) {
                throw new UnsupportedOperationException("The restaurant is not open at this time.");
            }

            if (deliveryTime.isBefore(order.getRestaurant().getOpeningTime().get()) || deliveryTime.isAfter(order.getRestaurant().getClosingTime().get())) {
                throw new UnsupportedOperationException("The restaurant is closed at this time.");
            }
        }

        // check that the delivery time is compatible with all the preparation time of the orders
        for (Order order : getOrders()) {
            if (deliveryTime.isBefore(LocalDateTime.now(clock).plusSeconds(order.getRestaurant().getPreparationTime(order.getItems())))) {
                throw new UnsupportedOperationException("The delivery time is too soon.");
            }
        }

        if (deliveryTime.isBefore(LocalDateTime.now(clock))) {
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
