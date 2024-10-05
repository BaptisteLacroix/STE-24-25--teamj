package fr.unice.polytech.equipe.j.equipe.j.user;

import java.util.UUID;

public class User {
    private String username;
    private String password;
    private UUID id;
    private double accountBalance;

    public User(String name, String password, double accountBalance) {
        this.username = name;
        this.password = password;
        this.accountBalance = accountBalance;
        this.id = UUID.randomUUID();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return username + " - " + accountBalance + "€";
    }
}
