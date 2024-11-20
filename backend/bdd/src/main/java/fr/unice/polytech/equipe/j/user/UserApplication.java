package fr.unice.polytech.equipe.j.user;

import fr.unice.polytech.equipe.j.FlexibleRestServer;

public class UserApplication {
    public static void main(String[] args) {
        FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j.user", 5004);
        server.start();
    }
}
