package fr.unice.polytech.equipe.j.user;

import fr.unice.polytech.equipe.j.order.DeliveryDetails;
import fr.unice.polytech.equipe.j.order.GroupOrder;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderBuilder;
import fr.unice.polytech.equipe.j.order.OrderStatus;
import fr.unice.polytech.equipe.j.payment.CheckoutObserver;
import fr.unice.polytech.equipe.j.payment.Transaction;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;
import fr.unice.polytech.equipe.j.restaurant.RestaurantProxy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConnectedUser extends User implements CheckoutObserver {
    private final Transaction transaction;
    private final List<Order> ordersHistory = new ArrayList<>();
    private GroupOrder groupOrder;
    private UUID currentOrder;

    public ConnectedUser(String email, String password, double accountBalance) {
        super(email, password, accountBalance);
        transaction = new Transaction(this);
        transaction.addObserver(this);
    }

    public void startOrder(RestaurantProxy restaurantProxy, UUID restaurantId) {
        currentOrder = restaurantProxy.startOrder(restaurantId);
    }

    public void addItemToOrder(RestaurantProxy restaurantProxy, UUID restaurantId, MenuItem item) {
        restaurantProxy.addItemToOrder(currentOrder, restaurantId, item);
    }

    public void proceedCheckout(RestaurantProxy restaurantProxy, UUID restaurantId) {
        Order order = restaurantProxy.validateOrder(currentOrder, restaurantId);
        transaction.proceedCheckout(order);
    }

    /**
     * Notify the user that the checkout was successful
     *
     * @param order The order that was successfully checked out
     */
    @Override
    public void notifyCheckoutSuccess(Order order) {
        addOrderToHistory(order);
        order.setStatus(OrderStatus.IN_PREPARATION);
    }

    /**
     * Add the order to the user's order history
     *
     * @param order The order to add
     */
    private void addOrderToHistory(Order order) {
        ordersHistory.add(order);
    }

    public List<Order> getOrdersHistory() {
        return ordersHistory;
    }

    /**
     * Create a group order
     * @param deliveryLocation The location where the order will be delivered
     * @param deliveryTime The time when the order will be delivered
     * @return The newly created group order
     */
    public GroupOrder createGroupOrder(String deliveryLocation, LocalDateTime deliveryTime) {
        if (deliveryLocation == null || deliveryLocation.isEmpty()) {
            throw new IllegalArgumentException("Delivery location must be specified.");
        }
        DeliveryDetails deliveryDetails = new DeliveryDetails(deliveryLocation, deliveryTime);
        groupOrder = new GroupOrder(deliveryDetails);
        return groupOrder;
    }

    public GroupOrder getGroupOrder() {
        return groupOrder;
    }

    @Override
    public String toString() {
        return super.toString() + " - " + getOrdersHistory().size() + " orders";
    }
}
