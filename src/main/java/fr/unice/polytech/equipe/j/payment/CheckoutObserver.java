package fr.unice.polytech.equipe.j.payment;

import fr.unice.polytech.equipe.j.order.Order;

public interface CheckoutObserver {
    void notifyCheckoutSuccess(Order order);
}
