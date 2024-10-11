package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class RestaurantProxy {
    private final List<Restaurant> restaurants;
    private final List<OrderBuilder> orderBuilders = new ArrayList<>();

    public RestaurantProxy(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    /**
     * Start the order through the proxy, ensuring only authorized users can create an order.
     * This method requests an OrderBuilder from the Restaurant and manages it internally.
     */
    public UUID startOrder(UUID restaurantId) {
        OrderBuilder orderBuilder = getRestaurant(restaurantId).createOrderBuilder();
        orderBuilders.add(orderBuilder);
        return orderBuilder.getOrderId();
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

    private Restaurant getRestaurant(UUID restaurantId) {
        return restaurants.stream()
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
}
