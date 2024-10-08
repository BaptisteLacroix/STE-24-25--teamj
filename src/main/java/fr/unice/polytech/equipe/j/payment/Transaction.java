package fr.unice.polytech.equipe.j.payment;

import fr.unice.polytech.equipe.j.user.ConnectedUser;

public class Transaction {
    private final ConnectedUser user;

    public Transaction(ConnectedUser user) {
        this.user = user;
    }
}
