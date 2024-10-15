package fr.unice.polytech.equipe.j.payment;

import java.util.UUID;

public interface CheckoutObserver {
    void orderPaid(UUID orderUUID);
}
