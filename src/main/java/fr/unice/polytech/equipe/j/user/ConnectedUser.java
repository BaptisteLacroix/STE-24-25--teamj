package fr.unice.polytech.equipe.j.user;

import fr.unice.polytech.equipe.j.order.GroupOrder;
import fr.unice.polytech.equipe.j.order.Order;
import fr.unice.polytech.equipe.j.payment.Transaction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ConnectedUser extends User {
    private final Transaction transaction;
    private GroupOrder groupOrder;
    private final List<Order> ordersHistory = new ArrayList<>();

    public ConnectedUser(String email, String password, double accountBalance) {
        super(email, password, accountBalance);
        transaction = new Transaction(this);
    }


    /**
     * Create a group order
     * @param deliveryLocation The location where the order will be delivered
     * @param deliveryTime The time when the order will be delivered
     * @return The newly created group order
     */
    public GroupOrder createGroupOrder(String deliveryLocation, LocalDateTime deliveryTime) {
        if (deliveryLocation == null || deliveryLocation.isEmpty()) {
            throw new IllegalArgumentException("Delivery location must be specified.");
        }
        groupOrder = new GroupOrder(deliveryLocation, deliveryTime);
        return groupOrder;
    }

    // Retrieve the group order history
    public GroupOrder getGroupOrder() {
        return groupOrder;
    }
}
