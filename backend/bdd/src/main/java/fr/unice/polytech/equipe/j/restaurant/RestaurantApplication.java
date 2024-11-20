package fr.unice.polytech.equipe.j.restaurant;

import fr.unice.polytech.equipe.j.FlexibleRestServer;

public class RestaurantApplication {
    public static void main(String[] args) {
        FlexibleRestServer server = new FlexibleRestServer("fr.unice.polytech.equipe.j", 5005);
        server.start();
    }
}
