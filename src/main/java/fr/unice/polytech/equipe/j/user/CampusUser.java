package fr.unice.polytech.equipe.j.user;

import fr.unice.polytech.equipe.j.delivery.DeliveryDetails;
import fr.unice.polytech.equipe.j.order.GroupOrder;
import fr.unice.polytech.equipe.j.order.IndividualOrder;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.payment.CheckoutObserver;
import fr.unice.polytech.equipe.j.payment.PaymentMethod;
import fr.unice.polytech.equipe.j.payment.Transaction;
import fr.unice.polytech.equipe.j.restaurant.MenuItem;
import fr.unice.polytech.equipe.j.order.OrderManager;
import fr.unice.polytech.equipe.j.restaurant.Restaurant;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CampusUser extends User implements CheckoutObserver {
    private final List<Transaction> transactions;
    private final List<Order> ordersHistory = new ArrayList<>();
    private Order currentOrder;
    private GroupOrder currentGroupOrder;

    private PaymentMethod defaultPaymentMethod = PaymentMethod.CREDIT_CARD;
    private final OrderManager orderManager;

    public CampusUser(String email, String password, OrderManager orderManager) {
        super(email, password);
        this.transactions = new ArrayList<>();
        this.orderManager = orderManager;
    }

    /**
     * Start an individual order
     */
    public void startIndividualOrder(Restaurant restaurant, DeliveryDetails deliveryDetails) {
        this.currentOrder = orderManager.startSingleOrder(restaurant, deliveryDetails);
    }

    /**
     * Start a group order
     *
     * @param deliveryDetails The delivery details
     */
    public void createGroupOrder(DeliveryDetails deliveryDetails) {
        this.currentGroupOrder = orderManager.startGroupOrder(deliveryDetails);
    }

    public void startSubGroupOrder(Restaurant restaurant) {
        this.currentOrder = orderManager.startSubGroupOrder(restaurant, getCurrentGroupOrder());
    }

    public PaymentMethod getDefaultPaymentMethod() {
        return defaultPaymentMethod;
    }

    public void setDefaultPaymentMethod(PaymentMethod defaultPaymentMethod) {
        this.defaultPaymentMethod = defaultPaymentMethod;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }



    public void joinGroupOrder(GroupOrder groupOrder) {
        orderManager.joinGroupOrder(groupOrder, this);
    }

    public void addItemToOrder(Restaurant restaurant, MenuItem item) {
        // TODO: change if the user can be part of multiple group orders
        if (currentGroupOrder != null) {
            orderManager.addItemToOrder(getCurrentGroupOrder(), getCurrentOrder(), restaurant, item);
        } else {
            orderManager.addItemToOrder((IndividualOrder) getCurrentOrder(), restaurant, item);
        }
    }

    public void validateOrder() {
        orderManager.setPreferedPaymenMethod(defaultPaymentMethod);
        transactions.add(orderManager.validateOrder( getCurrentOrder()));
        addOrderToHistory(getCurrentOrder());
    }

    public void validateOrderAndGroupOrder() {
        transactions.add(orderManager.validateOrder( getCurrentOrder()));
        addOrderToHistory(getCurrentOrder());
        orderManager.validateGroupOrder(currentGroupOrder);

    }

    public void validateOrderAndGroupOrder(LocalDateTime deliveryTime) {
        transactions.add(orderManager.validateOrder( getCurrentOrder()));
        addOrderToHistory(getCurrentOrder());
        orderManager.validateGroupOrder(currentGroupOrder, deliveryTime);
    }

    @Override
    public void orderPaid(Order order) {
        addOrderToHistory(order);
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

    public void setGroupOrder(GroupOrder groupOrder) {
        this.currentGroupOrder = groupOrder;
    }
}
