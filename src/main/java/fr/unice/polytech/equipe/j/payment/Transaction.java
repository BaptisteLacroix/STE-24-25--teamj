package fr.unice.polytech.equipe.j.payment;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.user.ConnectedUser;

import java.util.ArrayList;
import java.util.List;

public class Transaction {
    private final List<CheckoutObserver> observers = new ArrayList<>();

    public void addObserver(CheckoutObserver observer) {
        observers.add(observer);
    }

    private final ConnectedUser user;

    public Transaction(ConnectedUser user) {
        this.user = user;
    }

    public void proceedCheckout(Order order) {
        if (user.getAccountBalance() < order.getTotalPrice()) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        user.setAccountBalance(user.getAccountBalance() - order.getTotalPrice());

        for (CheckoutObserver observer : observers) {
            observer.notifyCheckoutSuccess(order);
        }
    }
}
