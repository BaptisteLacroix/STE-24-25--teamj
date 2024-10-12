package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.GroupOrder;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RestaurantProxy {
    private final List<OrderBuilder> orderBuilders = new ArrayList<>();
    private final List<GroupOrder> groupOrders = new ArrayList<>();

    public RestaurantProxy() {}

    /**
     * Start the order through the proxy, ensuring only authorized users can create an order.
     * This method requests an OrderBuilder from the Restaurant and manages it internally.
     */
    public UUID startSingleOrder(UUID restaurantId) {
        OrderBuilder orderBuilder = getRestaurant(restaurantId).createOrderBuilder();
        orderBuilders.add(orderBuilder);
        return orderBuilder.getOrderId();
    }

    /**
     * Start a group order.
     *
     * @param deliveryLocation The location to deliver to
     * @param deliveryTime     The time to deliver
     * @return The UUID of the group order
     */
    public UUID startGroupOrder(String deliveryLocation, LocalDateTime deliveryTime) {
        GroupOrder groupOrder = new GroupOrder(deliveryLocation, deliveryTime);
        groupOrders.add(groupOrder);
        return groupOrder.getGroupOrderId();
    }

    /**
     * Add a MenuItem to the current order, checking if it's available.
     *
     * @param menuItem The MenuItem to add
     * @throws IllegalArgumentException if the item is not available
     */
    public void addItemToOrder(UUID orderId, UUID restaurantId, MenuItem menuItem) {
        Restaurant restaurant = getRestaurant(restaurantId);
        OrderBuilder orderBuilder = getOrderBuilder(orderId);
        if (!restaurant.isItemAvailable(menuItem)) {
            throw new IllegalArgumentException("Item " + menuItem.getName() + " is not available.");
        }
        restaurant.addItemToOrder(orderBuilder, menuItem);
    }

    /**
     * Add an order to a group order.
     *
     * @param groupOrderId The group order to add to
     * @param orderId      The order to add
     */
    public void addOrderToGroup(UUID groupOrderId, UUID orderId) {
        GroupOrder groupOrder = getGroupOrder(groupOrderId);
        OrderBuilder orderBuilder = getOrderBuilder(orderId);
        Order order = orderBuilder.build();
        groupOrder.addOrder(order);
    }

    /**
     * Validate and finalize the order.
     *
     * @return The validated Order object
     */
    public Order validateOrder(UUID orderId, UUID restaurantId) {
        OrderBuilder orderBuilder = getOrderBuilder(orderId);
        Order order = orderBuilder.build();
        getRestaurant(restaurantId).addOrder(order);
        return order;
    }

    public GroupOrder validateGroupOrder(UUID groupOrderId) {
        GroupOrder groupOrder = getGroupOrder(groupOrderId);
        groupOrder.getOrders().forEach(order -> getRestaurant(order.getRestaurant().getRestaurantId()).addOrder(order));
        return groupOrder;
    }

    private Restaurant getRestaurant(UUID restaurantId) {
        return RestaurantManager.getRestaurants().stream()
                .filter(restaurant -> restaurant.getRestaurantId().equals(restaurantId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Restaurant with id: " + restaurantId + " not found."));
    }

    private OrderBuilder getOrderBuilder(UUID orderId) {
        return orderBuilders.stream()
                .filter(builder -> builder.getOrderId().equals(orderId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Order with id: " + orderId + " not found."));
    }

    private GroupOrder getGroupOrder(UUID groupOrderId) {
        return groupOrders.stream()
                .filter(group -> group.getGroupOrderId().equals(groupOrderId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("GroupOrder with id: " + groupOrderId + " not found."));
    }
}
