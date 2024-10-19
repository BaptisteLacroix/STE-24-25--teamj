package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.DeliveryDetails;
import fr.unice.polytech.equipe.j.order.GroupOrder;
import fr.unice.polytech.equipe.j.order.GroupOrderFacade;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderBuilder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class RestaurantProxy {
    private final List<Restaurant> restaurants;
    private final List<OrderBuilder> orderBuilders = new ArrayList<>();
    private final Set<GroupOrder> groupOrders = new HashSet<>();

    public RestaurantProxy(List<Restaurant> restaurants) {
        this.restaurants = restaurants;
    }

    /**
     * Start the order through the proxy, ensuring only authorized users can create an order.
     * This method requests an OrderBuilder from the Restaurant and manages it internally.
     */
    public UUID startOrder(UUID restaurantId) {
        OrderBuilder orderBuilder = new OrderBuilder()
                .setRestaurant(getRestaurant(restaurantId));
        orderBuilders.add(orderBuilder);
        return orderBuilder.getOrderId();
    }

    /**
     * Start a group order.
     *
     * @param deliveryDetails The delivery details for the group order
     * @return The UUID of the group order
     */
    public UUID startGroupOrder(DeliveryDetails deliveryDetails) {
        GroupOrder groupOrder = new GroupOrder(deliveryDetails);
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

    public void changeGroupDeliveryTime(UUID currenGroupOrder, LocalDateTime deliveryTime) {
        GroupOrder groupOrder = groupOrders.stream()
                .filter(order -> order.getGroupOrderId().equals(currenGroupOrder))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Group order with id: " + currenGroupOrder + " not found."));
        groupOrder.setDeliveryTime(deliveryTime);
    }

    public GroupOrderFacade getGroupOrder(UUID groupOrderId) {
        GroupOrder groupOrder = groupOrders.stream()
                .filter(order -> order.getGroupOrderId().equals(groupOrderId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Group order with id: " + groupOrderId + " not found."));
        return new GroupOrderFacade(groupOrder);
    }
}
