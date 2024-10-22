package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderStatus;

import java.time.Clock;
import java.time.LocalDateTime;

public class RestaurantProxy implements IRestaurant {
    private final Clock clock;
    private final Restaurant restaurant;

    public Clock getClock() {
        return clock;
    }

    public RestaurantProxy(Restaurant restaurant, Clock clock) {
        this.clock = clock;
        this.restaurant = restaurant;
    }

    @Override
    public void addOrder(Order order, DeliveryDetails deliveryDetails) {
        if (this.capacityCheck() && this.canPrepareItemForDeliveryTime(deliveryDetails)) {
            //restaurant.addOrder(order, deliveryDetails);
        }
    }

    @Override
    public void cancelOrder(Order order) {
        restaurant.cancelOrder(order);
        order.setStatus(OrderStatus.CANCELLED);
    }

    @Override
    public void orderPaid(Order order) {
        restaurant.orderPaid(order);
    }

    /**
     * Check if the restaurant has enough capacity to accept a new order.
     *
     * @return true if there is capacity, false otherwise.
     */
    public boolean capacityCheck() {
        return restaurant.getSlots().stream().anyMatch(slot -> slot.getAvailableCapacity() > 0);
    }

    /**
     * Check if the restaurant can prepare any item in time for the delivery
     *
     * @param deliveryDetails de
     * @return true if the restaurant can prepare any item in time, false otherwise
     */
    public boolean canPrepareItemForDeliveryTime(DeliveryDetails deliveryDetails) {
        // Check that the delivery time is not empty
        if (deliveryDetails.getDeliveryTime().isEmpty()) {
            return true;
        }

        LocalDateTime now = LocalDateTime.now(getClock());
        LocalDateTime deliveryTime = deliveryDetails.getDeliveryTime().get();
        LocalDateTime closingTime = restaurant.getClosingTime().get();

        // Check if the restaurant can prepare any item before the closing time or the delivery time
        return restaurant.getMenu().getItems().stream()
                .anyMatch(item -> {
                    LocalDateTime preparationEndTime = now.plusSeconds(item.getPrepTime());
                    return preparationEndTime.isBefore(deliveryTime) && preparationEndTime.isBefore(closingTime);
                });
    }
}
