package fr.unice.polytech.equipe.j.user;

import java.util.UUID;

public class User {
    private String email;
    private String password;
    private UUID id;
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

    public void setEmail(String email) {
        this.email = email;
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
        return email + " - " + accountBalance + "€";
    }
}
