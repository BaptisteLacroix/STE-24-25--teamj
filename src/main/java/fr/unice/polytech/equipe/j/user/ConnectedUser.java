package fr.unice.polytech.equipe.j.user;

import fr.unice.polytech.equipe.j.order.DeliveryDetails;
import fr.unice.polytech.equipe.j.order.GroupOrder;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.payment.CheckoutObserver;
import fr.unice.polytech.equipe.j.payment.Transaction;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.order.OrderManager;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;

public class ConnectedUser extends User implements CheckoutObserver {
    private final Transaction transaction;
    private final List<Order> ordersHistory = new ArrayList<>();
    private Order currentOrder;
    private GroupOrder currentGroupOrder;
    private final OrderManager orderManager;

    public ConnectedUser(String email, String password, double accountBalance, OrderManager orderManager) {
        super(email, password, accountBalance);
        transaction = new Transaction(this);
        transaction.addObserver(this);
        this.orderManager = orderManager;
    }

    /**
     * Start an individual order
     */
    public void startIndividualOrder(Restaurant restaurant, DeliveryDetails deliveryDetails) {
        this.currentOrder = new OrderManager().startSingleOrder(restaurant, deliveryDetails);
    }

    /**
     * Start a group order
     *
     * @param deliveryDetails The delivery details
     */
    public void startGroupOrder(DeliveryDetails deliveryDetails) {
        this.currentGroupOrder = new OrderManager().startGroupOrder(deliveryDetails);
    }

    public void addItemToOrder(Restaurant restaurant, MenuItem item) {
        new OrderManager().addItemToOrder(getCurrentOrder(), restaurant, item);
    }

    public void validateIndividualOrder(Restaurant restaurant) {
        new OrderManager().validateIndividualOrder(transaction, getCurrentOrder(), restaurant);
    }

    @Override
    public void orderPaid(Order order) {
        addOrderToHistory(order);
    }

    private void addOrderToHistory(Order order) {
        ordersHistory.add(order);
    }

    public List<Order> getOrdersHistory() {
        return ordersHistory;
    }

    public Order getCurrentOrder() {
        return currentOrder;
    }

    @Override
    public String toString() {
        return super.toString() + " - " + getOrdersHistory().size() + " orders";
    }

    public GroupOrder getCurrentGroupOrder() {
        return currentGroupOrder;
    }
}
