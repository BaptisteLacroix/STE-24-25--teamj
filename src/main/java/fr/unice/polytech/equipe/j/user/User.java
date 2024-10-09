package fr.unice.polytech.equipe.j.user;

import java.util.UUID;

public class User {
    private final String email;
    private String password;
    private final UUID id;
    private double accountBalance;

    public User(String email, String password, double accountBalance) {
        this.email = email;
        this.password = password;
        this.accountBalance = accountBalance;
        this.id = UUID.randomUUID();
    }

    public String getEmail() {
        return email;
    }

    public UUID getId() {
        return id;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    @Override
    public String toString() {
        return getEmail() + " - " + getAccountBalance() + "€";
    }
}
