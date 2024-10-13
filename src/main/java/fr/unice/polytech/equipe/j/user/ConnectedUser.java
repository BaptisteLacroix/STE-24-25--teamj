package fr.unice.polytech.equipe.j.user;

import fr.unice.polytech.equipe.j.order.DeliveryDetails;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.order.OrderStatus;
import fr.unice.polytech.equipe.j.payment.CheckoutObserver;
import fr.unice.polytech.equipe.j.payment.Transaction;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.restaurant.RestaurantProxy;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConnectedUser extends User implements CheckoutObserver {
    private final Transaction transaction;
    private final List<Order> ordersHistory = new ArrayList<>();
    private UUID currentOrder;
    private UUID currentGroupOrder;

    public ConnectedUser(String email, String password, double accountBalance) {
        super(email, password, accountBalance);
        transaction = new Transaction(this);
        transaction.addObserver(this);
    }

    /**
     * Start an individual order
     *
     * @param restaurantProxy The restaurant proxy
     * @param restaurantId    The restaurant ID
     */
    public void startIndividualOrder(RestaurantProxy restaurantProxy, UUID restaurantId) {
        this.currentOrder = restaurantProxy.startSingleOrder(restaurantId);
    }

    /**
     * Start a group order
     *
     * @param restaurantProxy The restaurant proxy
     * @param deliveryDetails The delivery details
     */
    public void startGroupOrder(RestaurantProxy restaurantProxy, DeliveryDetails deliveryDetails) {
        this.currentOrder = restaurantProxy.startGroupOrder(deliveryDetails);
    }

    /**
     * Add an item to the current order
     *
     * @param restaurantProxy The restaurant proxy
     * @param restaurantId    The restaurant ID
     * @param item            The item to add
     */
    public void addItemToOrder(RestaurantProxy restaurantProxy, UUID restaurantId, MenuItem item) {
        restaurantProxy.addItemToOrder(currentOrder, restaurantId, item);
    }

    /**
     * Add an item to the current group order
     *
     * @param restaurantProxy The restaurant proxy
     */
    public void validateGroupOrder(RestaurantProxy restaurantProxy) {
        restaurantProxy.validateGroupOrder(currentGroupOrder);
    }

    /**
     * Validate the individual order
     *
     * @param restaurantProxy The restaurant proxy
     * @param restaurantId    The restaurant ID
     */
    public void validateIndividualOrder(RestaurantProxy restaurantProxy, UUID restaurantId) {
        restaurantProxy.validateIndividualOrder(currentOrder, restaurantId);
    }

    /**
     * Add an order to the group order
     */
    public void proceedIndividualOrderCheckout() {
        // TODO: voir comment différencier commande individuelle et commande de groupe
        transaction.proceedCheckout(currentOrder);
    }

    /**
     * Notify the user that the checkout was successful
     *
     * @param order The order that was successfully checked out
     */
    @Override
    public void notifyCheckoutSuccess(Order order) {
        addOrderToHistory(order);
        order.setStatus(OrderStatus.VALIDATED);
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

    @Override
    public String toString() {
        return super.toString() + " - " + getOrdersHistory().size() + " orders";
    }
}
