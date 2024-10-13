package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.order.DeliveryDetails;
import fr.unice.polytech.equipe.j.order.GroupOrder;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderBuilder;
import fr.unice.polytech.equipe.j.order.OrderStatus;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RestaurantProxy {
    private final Set<OrderBuilder> orderBuilders = new HashSet<>();
    private final Set<GroupOrder> groupOrders = new HashSet<>();

    /**
     * Start the order through the proxy, ensuring only authorized users can create an order.
     * This method requests an OrderBuilder from the Restaurant and manages it internally.
     */
    public UUID startSingleOrder(UUID restaurantId) {
        OrderBuilder orderBuilder = getRestaurant(restaurantId).createOrderBuilder();
        orderBuilders.add(orderBuilder);
        return orderBuilder.getOrderId();
    }

    public void cancelOrder(UUID orderId) {
        RestaurantFacade restaurant = getRestaurantFromOrderId(orderId);
        RestaurantServiceManager.getInstance().getRestaurant(restaurant.getRestaurantId()).cancelOrder(orderId);
    }

    private RestaurantFacade getRestaurantFromOrderId(UUID orderId) {
        return orderBuilders.stream()
                .filter(builder -> builder.getOrderId().equals(orderId))
                .findFirst()
                .map(OrderBuilder::getRestaurant)
                .orElseThrow(() -> new IllegalArgumentException("Order with id: " + orderId + " not found."));
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
     * Validate and finalize the individual order.
     */
    public void validateIndividualOrder(UUID orderId, UUID restaurantId) {
        OrderBuilder orderBuilder = getOrderBuilder(orderId);
        Order order = orderBuilder.build();
        getRestaurant(restaurantId).addOrder(order);
    }

    /**
     * Validate and finalize the group order.
     *
     * @param groupOrderId The group order to validate
     */
    public void validateGroupOrder(UUID groupOrderId) {
        GroupOrder groupOrder = getGroupOrder(groupOrderId);
        // for each order in the group order that are OrderStatus.VALIDATED, add them to the restaurant
        groupOrder.getOrders().stream()
                .filter(order -> order.getStatus() == OrderStatus.VALIDATED)
                .forEach(order -> getRestaurant(order.getRestaurantFacade().getRestaurantId()).addOrder(order));
    }

    private Restaurant getRestaurant(UUID restaurantId) {
        Restaurant restaurant = RestaurantServiceManager.getInstance().getRestaurant(restaurantId);
        if (restaurant == null) {
            throw new IllegalArgumentException("Restaurant with id: " + restaurantId + " not found.");
        }
        return restaurant;
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
