package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.user.CampusUser;

import java.time.LocalDateTime;
import java.util.*;

public class GroupOrder {
    private final UUID groupOrderId;
    private final List<Order> orders = new ArrayList<>();
    private final List<CampusUser> users = new ArrayList<>();
    private final DeliveryDetails deliveryDetails;
    private OrderStatus status = OrderStatus.PENDING;


    public void addUser(CampusUser user) {
        if (this.status == OrderStatus.VALIDATED)
            throw new IllegalStateException("The group order has already been validated.");
        this.users.add(user);
    }
    public GroupOrder(DeliveryDetails deliveryDetails) {
        this.groupOrderId = UUID.randomUUID();
        this.deliveryDetails = deliveryDetails;
    }

    // Add an individual order to the group
    public void addOrder(Order order) {
        if (this.status != OrderStatus.PENDING)
            throw new IllegalStateException("Cannot join a group order that is not pending");
        // FIXME adding the order to do the check and remove in case of bad check might not be thread safe
        this.orders.add(order);
        Restaurant restaurant = order.getRestaurant();
        if(!restaurant.capacityCheck() || !restaurant.canPrepareItemForGroupOrderDeliveryTime(this)) {
            this.orders.remove(order);
            throw new IllegalStateException("Order cannot be added, restaurant does not have the capacity to deliver in time");
        }
        order.setOnItemAdded((menuItem -> {
            this.checkOrderUpdate(order, menuItem);
        }));
    }

    private void checkOrderUpdate(Order order, MenuItem menuItem) {
        // Check if the item can be prepared in time for the delivery
        Optional<LocalDateTime> deliveryTime = this.deliveryDetails.getDeliveryTime();
        LocalDateTime estimatedReadyTime = TimeUtils.getNow().plusSeconds(menuItem.getPrepTime());

        if (deliveryTime.isPresent() && estimatedReadyTime.isAfter(deliveryTime.orElseThrow())){
            throw new IllegalArgumentException("Cannot add item to order, it will not be ready in time.");
        }

        if (deliveryTime.isPresent() && !order.getRestaurant().slotAvailable(menuItem, deliveryTime.get())) {
            throw new IllegalArgumentException("Cannot add item to order, no slot available.");
        }
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

    public Optional<LocalDateTime> getPossibleDeliveryTime() {
        // If the delivery tim is not set return the latest delivery time possible from the orders items
        if (deliveryDetails.getDeliveryTime().isEmpty()) {
            Optional<LocalDateTime> latestDeliveryTime = Optional.empty();
            for (Order order : orders) {
                int preparationTime = order.getRestaurant().getPreparationTime(order.getItems());
                LocalDateTime orderDeliveryTime = TimeUtils.getNow().plusSeconds(preparationTime);
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
            if (deliveryTime.isBefore(TimeUtils.getNow().plusSeconds(order.getRestaurant().getPreparationTime(order.getItems())))) {
                throw new UnsupportedOperationException("The delivery time is too soon.");
            }
        }

        if (deliveryTime.isBefore(TimeUtils.getNow())) {
            throw new UnsupportedOperationException("You cannot specify a delivery time in the past.");
        }
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
