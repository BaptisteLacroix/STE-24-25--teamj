package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.TimeUtils;
import fr.unice.polytech.equipe.j.order.GroupOrderProxy;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class RestaurantProxy {
    private final Restaurant restaurant;

    public RestaurantProxy(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    /**
     * Add an item to an order, either individual or group.
     *
     * @param order        the order to which the item is added
     * @param menuItem     the menu item being added
     * @param deliveryTime the delivery time for the order (optional)
     * @throws IllegalArgumentException if the item is not available or cannot be prepared in time
     */
    // @Override
    public void addItemToOrder(Order order, MenuItem menuItem, LocalDateTime deliveryTime) {
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalArgumentException("Cannot add items to an order that is not pending.");
        }
        if (!getRestaurant().isItemAvailable(menuItem)) {
            throw new IllegalArgumentException("Item is not available.");
        }
        // Check if the item can be prepared in time for the delivery
        if (deliveryTime != null && isItemTooLate(menuItem, deliveryTime)) {
            throw new IllegalArgumentException("Cannot add item to order, it will not be ready in time.");
        }
        if (deliveryTime != null && !getRestaurant().slotAvailable(menuItem, deliveryTime)) {
            throw new IllegalArgumentException("Cannot add item to order, no slot available.");
        }
        // If no delivery time set, so it's a group order, and we check that the order can be prepared before the closing time
        if (deliveryTime == null && isItemTooLate(menuItem, getRestaurant().getClosingTime().get())) {
            throw new IllegalArgumentException("Cannot add item to order, restaurant cannot prepare it.");
        }
        restaurant.addItemToOrder(order, menuItem, deliveryTime);
    }

    /**
     * Helper method to check if the item's preparation time exceeds the delivery time.
     */
    private boolean isItemTooLate(MenuItem menuItem, LocalDateTime deliveryTime) {
        LocalDateTime estimatedReadyTime = TimeUtils.getNow().plusSeconds(menuItem.getPrepTime());
        return estimatedReadyTime.isAfter(deliveryTime);
    }

    // @Override
    public void cancelOrder(Order order, LocalDateTime deliveryTime) {
        // If the delivery time is not half an hour before the order, the order is cancelled
        if (deliveryTime.isBefore(LocalDateTime.now().minusMinutes(30))) {
            restaurant.cancelOrder(order);
            order.setStatus(OrderStatus.CANCELLED);
        }
    }

    // @Override
    public void checkOrderCanBeValidated(Order order) {
        getRestaurant().checkOrderCanBeValidated(order);
    }

    public boolean canAccommodateDeliveryTime(List<MenuItem> items, LocalDateTime deliveryTime) {
        return getRestaurant().canAccommodateDeliveryTime(items, deliveryTime);
    }

    // @Override
    public double getTotalPrice(Order order) {
        return getRestaurant().getTotalPrice(order);
    }

    public Menu getMenu() {
        return getRestaurant().getMenu();
    }

    public Optional<LocalDateTime> getOpeningTime() {
        return getRestaurant().getOpeningTime();
    }

    public Optional<LocalDateTime> getClosingTime() {
        return getRestaurant().getClosingTime();
    }

    // @Override
    public void onOrderPaid(Order order) {
        getRestaurant().onOrderPaid(order);
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public boolean canPrepareItemForGroupOrderDeliveryTime(GroupOrderProxy groupOrderProxy) {
        return restaurant.canPrepareItemForGroupOrderDeliveryTime(groupOrderProxy);
    }

    public UUID getRestaurantUUID() {
        return restaurant.getRestaurantId();
    }

    public void setOrderPriceStrategy(OrderPriceStrategy orderPriceStrategy) {
        restaurant.setOrderPriceStrategy(orderPriceStrategy);
    }

    public OrderPrice processOrderPrice(Order order) {
        return restaurant.processOrderPrice(order);
    }
}
