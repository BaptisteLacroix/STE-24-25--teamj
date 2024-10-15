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
    private final List<UUID> ordersHistory = new ArrayList<>();
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
     * @param restaurantId    The restaurant ID
     */
    public void startIndividualOrder(UUID restaurantId) {
        this.currentOrder = RestaurantProxy.getInstance().startSingleOrder(restaurantId);
    }

    /**
     * Start a group order
     *
     * @param deliveryDetails The delivery details
     */
    public void startGroupOrder(DeliveryDetails deliveryDetails) {
        this.currentOrder = RestaurantProxy.getInstance().startGroupOrder(deliveryDetails);
    }

    /**
     * Add an item to the current order
     *
     * @param restaurantId    The restaurant ID
     * @param item            The item to add
     */
    public void addItemToOrder(UUID restaurantId, MenuItem item) {
        RestaurantProxy.getInstance().addItemToOrder(currentOrder, restaurantId, item);
    }

    /**
     * Add an item to the current group order
     *
     */
    public void validateGroupOrder() {
        RestaurantProxy.getInstance().validateGroupOrder(currentGroupOrder);
    }

    /**
     * Validate the individual order
     *
     * @param restaurantId    The restaurant ID
     */
    public void validateIndividualOrder(UUID restaurantId) {
        // TODO: Proceed to checkout
        RestaurantProxy.getInstance().validateIndividualOrder(currentOrder, restaurantId);
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
    public void orderPaid(UUID order) {
        addOrderToHistory(order);
    }

    /**
     * Add the order to the user's order history
     *
     * @param order The order to add
     */
    private void addOrderToHistory(UUID order) {
        ordersHistory.add(order);
    }

    public List<UUID> getOrdersHistory() {
        return ordersHistory;
    }

    public OrderStatus getCurrentOrderState(UUID restaurantUUID, UUID orderUUID) {
        return RestaurantProxy.getInstance().getOrderStatus(restaurantUUID, orderUUID);
    }

    public UUID getCurrentOrder() {
        return currentOrder;
    }

    @Override
    public String toString() {
        return super.toString() + " - " + getOrdersHistory().size() + " orders";
    }
}
