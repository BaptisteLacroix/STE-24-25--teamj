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

    private final ConnectedUser user;

    public Transaction(ConnectedUser user) {
        this.user = user;
    }

    public void proceedCheckout(UUID orderUUID) {
        Order order = RestaurantServiceManager.getInstance().getOrder(orderUUID);
        if (user.getAccountBalance() < order.getTotalPrice()) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        user.setAccountBalance(user.getAccountBalance() - order.getTotalPrice());

        // TODO: Call the restaurant to update the status of the command, VALIDATED if payment is successful

        for (CheckoutObserver observer : observers) {
            observer.orderPaid(orderUUID);
        }
    }
}
