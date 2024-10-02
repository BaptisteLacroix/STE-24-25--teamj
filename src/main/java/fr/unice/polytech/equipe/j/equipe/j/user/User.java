package fr.unice.polytech.equipe.j.equipe.j.user;

import java.util.UUID;

public class User {
    private String name;
    private UUID id;
    private double accountBalance;

    public User(String name, double accountBalance) {
        this.name = name;
        this.accountBalance = accountBalance;
        this.id = UUID.randomUUID();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }
}
