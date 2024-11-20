package fr.unice.polytech.equipe.j.order;

import fr.unice.polytech.equipe.j.FlexibleRestServer;

public class OrderApplication {
    public static void main(String[] args) {
        FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j", 5006);
        server.start();
    }
}
