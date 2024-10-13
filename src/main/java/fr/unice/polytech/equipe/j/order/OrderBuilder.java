package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.RestaurantFacade;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class OrderBuilder {
    private RestaurantFacade restaurantFacade;
    private final List<MenuItem> items = new ArrayList<>();
    private DeliveryDetails deliveryDetails;
    private final UUID orderId = UUID.randomUUID();
    private boolean isIndividualOrder = false;  // Flag to determine the order type

    /**
     * Set the restaurant for the order
     *
     * @param restaurantFacade The restaurant facade
     * @return The OrderBuilder instance
     */
    public OrderBuilder setRestaurant(RestaurantFacade restaurantFacade) {
        this.restaurantFacade = restaurantFacade;
        return this;
    }

    /**
     * Add a MenuItem to the order
     *
     * @param item The MenuItem to add
     * @return The OrderBuilder instance
     */
    public OrderBuilder addMenuItem(MenuItem item) {
        items.add(item);
        return this;
    }

    /**
     * Add a list of MenuItems to the order
     *
     * @param items The list of MenuItems to add
     * @return The OrderBuilder instance
     */
    public OrderBuilder addMenuItems(List<MenuItem> items) {
        this.items.addAll(items);
        return this;
    }

    /**
     * Set the delivery details for the order
     *
     * @param deliveryDetails The delivery details
     * @return The OrderBuilder instance
     */
    public OrderBuilder setDeliveryDetails(DeliveryDetails deliveryDetails) {
        this.deliveryDetails = deliveryDetails;
        return this;
    }

    /**
     * Build the order based on the type (Individual or Simple Order for Group)
     *
     * @return The Order instance (either Individual or Simple Order)
     */
    public Order build() {
        Order order;
        if (isIndividualOrder) {
            order = OrderFactory.createIndividualOrder(restaurantFacade, orderId, deliveryDetails);
        } else {
            order = OrderFactory.createSimpleOrder(restaurantFacade, orderId);
        }
        items.forEach(order::addItem);
        return order;
    }

    public UUID getOrderId() {
        return orderId;
    }

    public RestaurantFacade getRestaurant() {
        return restaurantFacade;
    }
}
