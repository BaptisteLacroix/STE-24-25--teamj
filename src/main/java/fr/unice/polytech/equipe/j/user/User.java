package fr.unice.polytech.equipe.j.user;

import java.util.UUID;

public class User {
    private final String email;
    private String password;
    private final UUID id;

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.id = UUID.randomUUID();
    }

    public String getEmail() {
        return email;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public String toString() {
        return getEmail();
    }
}
