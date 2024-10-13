package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.restaurant.RestaurantFacade;
import java.util.UUID;

public class OrderFactory {

    // Method to create an IndividualOrder
    public static Order createIndividualOrder(RestaurantFacade restaurantFacade, UUID orderId, DeliveryDetails deliveryDetails) {
        return new IndividualOrder(restaurantFacade, orderId, deliveryDetails);
    }

    // Method to create a Simple Order for a GroupOrder
    public static Order createSimpleOrder(RestaurantFacade restaurantFacade, UUID orderId) {
        return new Order(restaurantFacade, orderId);  // Create a basic order with no delivery details
    }
}
