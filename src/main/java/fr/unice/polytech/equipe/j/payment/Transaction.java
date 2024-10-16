package fr.unice.polytech.equipe.j.payment;

import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.restaurant.RestaurantServiceManager;
import fr.unice.polytech.equipe.j.user.ConnectedUser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Transaction {
    private final List<CheckoutObserver> observers = new ArrayList<>();

    public void addObserver(CheckoutObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(CheckoutObserver observer) {
        observers.remove(observer);
    }

    private final ConnectedUser user;

    public Transaction(ConnectedUser user) {
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
}
