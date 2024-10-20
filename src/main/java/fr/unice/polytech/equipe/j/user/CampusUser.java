package fr.unice.polytech.equipe.j.user;

import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.order.GroupOrder;
import fr.unice.polytech.equipe.j.order.IndividualOrder;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.payment.CheckoutObserver;
import fr.unice.polytech.equipe.j.payment.Transaction;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.order.OrderManager;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CampusUser extends User implements CheckoutObserver {
    private final Transaction transaction;
    private final List<Order> ordersHistory = new ArrayList<>();
    @Deprecated
    private Order currentOrder;
    @Deprecated
    private GroupOrder currentGroupOrder;

    public OrderManager getOrderManager() {
        return orderManager;
    }

    private final OrderManager orderManager;

    public CampusUser(String email, String password, OrderManager orderManager) {
        super(email, password);
        transaction = new Transaction(this);
        transaction.addObserver(this);
        this.orderManager = orderManager;
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

    @Deprecated
    public Order getCurrentOrder() {
        return currentOrder;
    }

    @Deprecated
    public GroupOrder getCurrentGroupOrder() {
        return currentGroupOrder;
    }

    @Deprecated
    public void setCurrentGroupOrder(GroupOrder currentGroupOrder) {
        this.currentGroupOrder = currentGroupOrder;
    }
    /**
     * Start an individual order
     */
//    @Deprecated
//    public void startIndividualOrder(Restaurant restaurant, DeliveryDetails deliveryDetails) {
//        this.currentOrder = orderManager.startSingleOrder(restaurant, deliveryDetails);
//    }

    /**
     * Start a group order
     *
     * @param deliveryDetails The delivery details
     */
    @Deprecated
    public void createGroupOrder(DeliveryDetails deliveryDetails) {
        this.currentGroupOrder = orderManager.startGroupOrder(deliveryDetails);
    }

//    @Deprecated
//    public void startSubGroupOrder(Restaurant restaurant) {
//        this.currentOrder = orderManager.startSubGroupOrder(restaurant, getCurrentGroupOrder());
//    }

    @Deprecated
    public void setCurrentOrder(Order currentOrder) {
        this.currentOrder = currentOrder;
    }

//    @Deprecated
//    public void joinGroupOrder(GroupOrder groupOrder) {
//        orderManager.joinGroupOrder(groupOrder, this);
//    }

    @Deprecated
    public void addItemToOrder(Restaurant restaurant, MenuItem item) {
        // TODO: change if the user can be part of multiple group orders
        if (currentGroupOrder != null) {
            orderManager.addItemToOrder(getCurrentGroupOrder(), getCurrentOrder(), restaurant, item);
        } else {
            orderManager.addItemToOrder((IndividualOrder) getCurrentOrder(), restaurant, item);
        }
    }

    /**
     * TODO : this method should be in the Order class
     */
    @Deprecated
    public void validateOrder() {
        orderManager.validateOrder(transaction, getCurrentOrder());
    }

    /**
     * TODO : this method should desctructured into two methds
     */
    @Deprecated
    public void validateOrderAndGroupOrder() {
        this.validateOrder();
        orderManager.validateGroupOrder(currentGroupOrder);
    }

    /**
     * TODO : this method should be in the Order class
     */
    @Deprecated
    public void validateOrderAndGroupOrder(LocalDateTime deliveryTime) {
        orderManager.validateOrder(transaction, getCurrentOrder());
        orderManager.validateGroupOrder(currentGroupOrder, deliveryTime);
    }

    @Override
    public void onOrderPaid(Order order) {
        addOrderToHistory(order);
    }
}
