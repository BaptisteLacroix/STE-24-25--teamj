package fr.unice.polytech.equipe.j.user;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderBuilder;
import fr.unice.polytech.equipe.j.payment.CheckoutObserver;
import fr.unice.polytech.equipe.j.payment.Transaction;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConnectedUser extends User implements CheckoutObserver {
    private final Transaction transaction;
    private OrderBuilder orderBuilder;
    private final List<Order> ordersHistory = new ArrayList<>();

    public ConnectedUser(String email, String password, double accountBalance) {
        super(email, password, accountBalance);
        transaction = new Transaction(this);
        transaction.addObserver(this);
    }

    /**
     * Start a new order with the selected restaurant
     */
    public void startOrder(Restaurant restaurant) {
        orderBuilder = new OrderBuilder().setRestaurant(restaurant);
    }

    /**
     * Add an item to the current order
     *
     * @param item The item to add
     */
    public void addItemToOrder(MenuItem item) {
        if (orderBuilder == null) {
            throw new IllegalStateException("No active order. Start an order first.");
        }
        orderBuilder.addMenuItem(item);
    }

    /**
     * Add multiple items to the current order
     *
     * @param deliveryTime The delivery time
     * @throws IllegalStateException if there is no active order
     */
    public void setOrderDeliveryTime(LocalDateTime deliveryTime) {
        if (orderBuilder == null) {
            throw new IllegalStateException("No active order. Start an order first.");
        }
        orderBuilder.setDeliveryTime(deliveryTime);
    }

    /**
     * Proceed to checkout the current order
     *
     * @throws IllegalStateException if there is no order to check out
     */
    public void proceedCheckout() {
        if (orderBuilder == null) {
            throw new IllegalStateException("No order to checkout");
        }
        Order order = orderBuilder.build();
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
        clearCurrentOrder();
    }

    /**
     * Add the order to the user's order history
     *
     * @param order The order to add
     */
    private void addOrderToHistory(Order order) {
        ordersHistory.add(order);
    }

    /**
     * Clear the current order builder
     */
    private void clearCurrentOrder() {
        orderBuilder = null;
    }

    public List<Order> getOrdersHistory() {
        return ordersHistory;
    }

    @Override
    public String toString() {
        return super.toString() + " - " + ordersHistory.size() + " orders";
    }
}
