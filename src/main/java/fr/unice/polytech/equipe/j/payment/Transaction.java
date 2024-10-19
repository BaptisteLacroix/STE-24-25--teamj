package fr.unice.polytech.equipe.j.payment;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.user.CampusUser;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private final List<CheckoutObserver> observers = new ArrayList<>();

    public void addObserver(CheckoutObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(CheckoutObserver observer) {
        observers.remove(observer);
    }

    private final CampusUser user;

    public Transaction(CampusUser user) {
        this.user = user;
    }

    public void proceedCheckout(Order order, double price) throws IllegalArgumentException {
        if (user.getAccountBalance() < price) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        user.setAccountBalance(user.getAccountBalance() - price);

        for (CheckoutObserver observer : observers) {
            // Call the restaurant and the user so the restaurant set the order as paid and the user can see the order uuid in his history
            observer.orderPaid(order);
        }
    }

    public List<CheckoutObserver> getObservers() {
        return observers;
    }
}
