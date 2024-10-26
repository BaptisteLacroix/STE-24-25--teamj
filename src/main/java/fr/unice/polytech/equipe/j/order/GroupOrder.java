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

//    private void checkOrderUpdate(Order order, MenuItem menuItem) {
//        // Check if the item can be prepared in time for the delivery
//        Optional<LocalDateTime> deliveryTime = this.deliveryDetails.getDeliveryTime();
//        LocalDateTime estimatedReadyTime = TimeUtils.getNow().plusSeconds(menuItem.getPrepTime());
//
//        if (deliveryTime.isPresent() && estimatedReadyTime.isAfter(deliveryTime.orElseThrow())){
//            throw new IllegalArgumentException("Cannot add item to order, it will not be ready in time.");
//        }
//
//        if (deliveryTime.isPresent() && !order.getRestaurant().slotAvailable(menuItem, deliveryTime.get())) {
//            throw new IllegalArgumentException("Cannot add item to order, no slot available.");
//        }
//    }


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

    // TODO: See later
//    public Optional<LocalDateTime> getPossibleDeliveryTime() {
//        // If the delivery tim is not set return the latest delivery time possible from the orders items
//        if (deliveryDetails.getDeliveryTime().isEmpty()) {
//            Optional<LocalDateTime> latestDeliveryTime = Optional.empty();
//            for (Order order : orders) {
//                int preparationTime = order.getRestaurant().getPreparationTime(order.getItems());
//                LocalDateTime orderDeliveryTime = TimeUtils.getNow().plusSeconds(preparationTime);
//                if (latestDeliveryTime.isEmpty() || orderDeliveryTime.isAfter(latestDeliveryTime.get())) {
//                    latestDeliveryTime = Optional.of(orderDeliveryTime);
//                }
//            }
//            return latestDeliveryTime;
//        }
//        return deliveryDetails.getDeliveryTime();
//    }

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
