package fr.unice.polytech.equipe.j.user;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.payment.strategy.PaymentMethod;
import fr.unice.polytech.equipe.j.payment.Transaction;

import java.util.ArrayList;
import java.util.List;

public class CampusUser {
    private PaymentMethod preferredPaymentMethod = PaymentMethod.CREDIT_CARD;
    private final List<Transaction> transactions;
    private final List<Order> ordersHistory = new ArrayList<>();
    private double balance;
    private String name;
    private PaymentMethod defaultPaymentMethod = PaymentMethod.CREDIT_CARD;

    public CampusUser(String name, double balance) {
        this.name = name;
        this.balance = balance;
        this.transactions = new ArrayList<>();
    }

    /**
     * Add the order to the user's order history
     *
     * @param order The order to add
     */
    public void addOrderToHistory(Order order) {
        ordersHistory.add(order);
    }

    public List<Order> getOrdersHistory() {
        return ordersHistory;
    }

    @Override
    public String toString() {
        return super.toString() + " - " + getOrdersHistory().size() + " orders";
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


    public void onOrderPaid(Order order) {
        addOrderToHistory(order);
    }

    public void addTransactionToHistory(Transaction transaction) {
        transactions.add(transaction);
    }
}
