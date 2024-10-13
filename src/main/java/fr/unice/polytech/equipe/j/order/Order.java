package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.RestaurantFacade;
import fr.unice.polytech.equipe.j.restaurant.RestaurantServiceManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Order {
    private final UUID orderUUID;
    private final RestaurantFacade restaurantFacade;
    private final List<MenuItem> items;
    private LocalDateTime deliveryTime;
    private OrderStatus status;

    public Order(RestaurantFacade restaurantFacade, UUID orderUUID) {
        this.restaurantFacade = restaurantFacade;
        items = new ArrayList<>();
        this.status = OrderStatus.PENDING;
        this.orderUUID = orderUUID;
    }

    public RestaurantFacade getRestaurantFacade() {
        return restaurantFacade;
    }

    public List<MenuItem> getItems() {
        return items;
    }

    public LocalDateTime getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(LocalDateTime deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public void addItem(MenuItem item) {
        if (getStatus().equals(OrderStatus.VALIDATED)) {
            throw new IllegalStateException("Cannot add items to an order that is already in preparation.");
        }
        items.add(item);
    }

    public void removeItem(MenuItem item) {
        if (getStatus().equals(OrderStatus.VALIDATED)) {
            throw new IllegalStateException("Cannot remove items from an order that is already in preparation.");
        }
        items.remove(item);
    }

    /**
     * Calculate the total price of the order
     *
     * @return The total price of the order
     */
    public double getTotalPrice() {
        return RestaurantServiceManager.getInstance().calculateOrderPriceFromRestaurant(restaurantFacade.getRestaurantId(), this);
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public UUID getOrderUUID() {
        return orderUUID;
    }

    @Override
    public String toString() {
        return "Order{" +
                "orderUUID=" + orderUUID +
                ", restaurantUUID=" + restaurantFacade +
                ", items=" + items +
                ", totalPrice=" + getTotalPrice() +
                ", status=" + status +
                ", deliveryTime=" + deliveryTime +
                '}';
    }
}
